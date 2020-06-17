package com.timing.seata.service;

import com.timing.seata.bean.Order;

public interface OrderService {
    Order create(String userId, String commodityCode, int orderCount);
}
