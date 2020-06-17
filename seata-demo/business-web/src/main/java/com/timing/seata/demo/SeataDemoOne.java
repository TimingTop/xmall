package com.timing.seata.demo;

import io.seata.core.exception.TransactionException;
import io.seata.rm.RMClient;
import io.seata.tm.TMClient;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;

public class SeataDemoOne {

    public void initSeata() throws TransactionException {

        // ====================    AT    ==============================
        // init seata
        TMClient.init("applicationId", "transactionServiceGroup");
        RMClient.init("a", "c");

        GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();

        try {
            tx.begin(60000, "testbeat");
            //  do something

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}
