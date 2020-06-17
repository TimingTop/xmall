package com.timing.seata.service;

public interface StorageService {

    /**
     * 扣减库存
     * @param commodityCode
     * @param count
     */
    void deduct(String commodityCode, int count);
}
