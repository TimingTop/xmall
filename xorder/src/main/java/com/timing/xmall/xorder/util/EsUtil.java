package com.timing.xmall.xorder.util;

import com.timing.xmall.xorder.config.ElasticsearchConfig;
import com.timing.xmall.xorder.entity.EsEntity;
import com.timing.xmall.xorder.entity.Order;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.IndexTemplatesExistRequest;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EsUtil {
    public static final String INDEX_TEMPLATE_NAME = "order_template";
    public static final String INDEX_TEMPLATE_JSON_FILE = "order_template.json";

    @Autowired
    private ElasticsearchConfig config;

    private RestHighLevelClient client;

    @PostConstruct
    public void init() {
        try {
            if (client != null) {
                client.close();
            }

            client = config.getClient();
            if (this.indexTemplateExist(INDEX_TEMPLATE_NAME)) {
                System.out.println("Index template exists");
                return;
            }


            String path = this.getClass().getClassLoader().getResource(INDEX_TEMPLATE_JSON_FILE).getPath();

            String indexTemplate = JsonUtil.readJsonFile(path);
            // create index template
            this.createIndexTemplate(INDEX_TEMPLATE_NAME, indexTemplate);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PreDestroy
    public void destroy() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public boolean indexTemplateExist(String indexTemplate) throws IOException {
        IndexTemplatesExistRequest request = new IndexTemplatesExistRequest(indexTemplate);
        request.setLocal(true);
        boolean flag = client.indices().existsTemplate(request, RequestOptions.DEFAULT);
        return flag;
    }

    public boolean createIndexTemplate(String templateName, String templateJson ) throws IOException {

        PutIndexTemplateRequest request = new PutIndexTemplateRequest(templateName);
        request.source(templateJson, XContentType.JSON);
        AcknowledgedResponse response = client.indices().putTemplate(request, RequestOptions.DEFAULT);
        if (!response.isAcknowledged()) {
            System.out.println("Index template has failed.");
            return false;
        } else {
            System.out.println("Index template has created successfully.");
        }
        return true;
    }

    /**
     *  判断 index 是否存在
     * @param index
     * @return
     * @throws IOException
     */
    public boolean indexExist(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);

        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 插入或者更新一个 document
     * @param index
     * @param entity
     * @return
     */
    public String insertOrUpdateOne(String index, EsEntity entity) {
        try {
            IndexRequest request = new IndexRequest(index);
            request.id(entity.getId());
            String jsonStr = JsonUtil.toJsonString(entity.getData());
            System.out.println("document: " + jsonStr);
            request.source(jsonStr, XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            return response.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();

            List<T> results = Stream.of(hits)
                    .map(x -> {
                        return JsonUtil.parseObject(x.getSourceAsString(), c);
                    })
                    .collect(Collectors.toList());

            return results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void search() {

        // 最原始的搜索
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("memo", "today");
        // 开启模糊匹配
        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
        // 对结果进行重新排序
        FieldValueFactorFunctionBuilder scoreFunctionBuilder = ScoreFunctionBuilders.fieldValueFactorFunction("count").factor(2.0f)
                .modifier(FieldValueFactorFunction.Modifier.LOG1P);

        //
        FunctionScoreQueryBuilder queryBuilder = QueryBuilders.functionScoreQuery(matchQueryBuilder, scoreFunctionBuilder);
        queryBuilder.boostMode(CombineFunction.SUM);


//        client.search()


    }

    public static void main(String[] args) throws IOException {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.56.103", 9200, "http"))
        );

//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("memo", "Today");
//        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
//
////        ScoreFunctionBuilder scoreFunctionBuilder = ScoreFunctionBuilders.fieldValueFactorFunction()
//
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.field("memo").requireFieldMatch(false);
//
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(matchQueryBuilder);
//        searchSourceBuilder.highlighter(highlightBuilder);
//
//
//        SearchRequest searchRequest = new SearchRequest("order");
//        searchRequest.source(searchSourceBuilder);
//
//        SearchResponse result = client.search(searchRequest, RequestOptions.DEFAULT);
//
//        System.out.println(result.getHits().getTotalHits());

//        findAll(client);
        findBySenderName(client);

    }

    public static void findAll(RestHighLevelClient client) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse result = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(result.getHits().getTotalHits());
    }

    public static void findAllIndex(RestHighLevelClient client) {

    }

    public static void findBySenderName(RestHighLevelClient client) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("senderName", "senderName1*"));
        sourceBuilder.from(0);
        sourceBuilder.size(5);

        SearchRequest searchRequest = new SearchRequest();

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response.getHits().getTotalHits());
    }






}
