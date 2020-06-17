package com.timing.seata.service.impl;

import com.timing.seata.service.StorageService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void deduct(String commodityCode, int count) {

        String sql = "update t_storage set count = count - "
                + count
                + " where commodity_code = '"
                + commodityCode
                + "'";

        jdbcTemplate.update(sql);
    }
}
