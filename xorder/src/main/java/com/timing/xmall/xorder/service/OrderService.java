package com.timing.xmall.xorder.service;

import com.timing.xmall.xorder.entity.EsEntity;
import com.timing.xmall.xorder.entity.Order;
import com.timing.xmall.xorder.util.EsUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private EsUtil esUtil;

    public String addOrder(Order order) {
        EsEntity<Order> esEntity = new EsEntity<>();
        esEntity.setId(String.valueOf(order.getOrderId()));
//        esEntity.setIndexName(order.getIndexName());
        esEntity.setData(order);


        // 按月 建  order 的  index ，是按月 按月
        return esUtil.insertOrUpdateOne(order.getIndexName(), esEntity);
    }

    public Order getByOrderId(long orderId) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(new TermQueryBuilder("orderid", orderId));
        // index 可以写 前缀
        List<Order> orders = esUtil.search("order", builder, Order.class);
        if (orders.size() > 0) {
            return orders.get(0);
        } else {
            return null;
        }
    }

    public List<Order> searchByCreateTime(Date beginTime, Date endTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginStr = df.format(beginTime);
        String endStr = df.format(endTime);

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("create_time")
                .from(beginStr)
                .to(endStr));
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(10).query(boolQueryBuilder);

        return esUtil.search("order", builder, Order.class);
    }

    public List<Order> searchByKeywords(List<String> keywords) {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

//        TermsAggregationBuilder aggregationBuilder = AggregationBuilders
////                .terms();
////        AggregationBuilders.ter
////        sourceBuilder.query(
////                QueryBuilders.termQuery()
////                .
////        )

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.trackScores(true);
        builder.size(10);
        builder.from(1000);

        // 高亮查询结果
        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.field(Field)

        builder.highlighter(highlightBuilder);

        return null;
    }
}
