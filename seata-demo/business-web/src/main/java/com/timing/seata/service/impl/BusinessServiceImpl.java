package com.timing.seata.service.impl;

import com.timing.seata.service.BusinessService;
import com.timing.seata.service.OrderService;
import com.timing.seata.service.StorageService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Reference(retries = 0, timeout = 30000)
    private OrderService orderService;

    @Reference(retries = 0, timeout = 30000)
    private StorageService storageService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @GlobalTransactional(name = "seata-dubbo-example001", timeoutMills = 300000)
    public void purchase(String userId, String commodityCode, int orderCount) {

        // 全局事务
        String xid = RootContext.getXID();
        System.out.println("xid: " + xid);

        // 1 先扣减 库存
        storageService.deduct(commodityCode, orderCount);
        // 2 再扣钱
        orderService.create(userId, commodityCode, orderCount);


    }


    public void test() {
        jdbcTemplate.execute("update t_account set money= money - 2 where id = 2;");

        jdbcTemplate.execute("INSERT into t_account(user_id, money) values ('7', 1000)");
    }
}
