package com.timing.seata.service;

import java.math.BigDecimal;

public interface AccountService {
    /*
    余额扣款
     */
    void debit(String userId, BigDecimal money);
}


