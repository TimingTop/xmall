package com.timing.seata.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Account implements Serializable {

    private long id;

    private String userId;

    private BigDecimal money;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
