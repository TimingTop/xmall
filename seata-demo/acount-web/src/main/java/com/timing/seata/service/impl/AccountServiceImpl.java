package com.timing.seata.service.impl;

import com.timing.seata.service.AccountService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void debit(String userId, BigDecimal money) {



        String sql = "update t_account set money = money - "
                + money.doubleValue()
                + " where user_id = "
                + userId;

        jdbcTemplate.update(sql);


    }
}
