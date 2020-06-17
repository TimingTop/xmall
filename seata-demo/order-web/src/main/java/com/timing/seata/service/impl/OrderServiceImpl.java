package com.timing.seata.service.impl;

import com.timing.seata.bean.Order;
import com.timing.seata.service.AccountService;
import com.timing.seata.service.OrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {

    @Reference(retries = 0, timeout = 30000)
    private AccountService accountService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Order create(String userId, String commodityCode, int orderCount) {
        BigDecimal orderMoney = calculate(commodityCode, orderCount);


        accountService.debit(userId, orderMoney);

        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(orderCount);
        order.setMoney(orderMoney);

        String sql = "insert into t_order(user_id, commodity_code, count, money) values ('"
                + userId + "','"
                + commodityCode + "','"
                + orderCount + "','"
                + orderMoney + "'"
                + ")";
        jdbcTemplate.execute(sql);

        return order;
    }

    protected BigDecimal calculate(String commodityCode, int orderCount) {
        return BigDecimal.valueOf(100);
    }
}
