package com.timing.xmall.xorder.entity;

import java.util.Calendar;
import java.util.Date;

public class Order {

    private long orderId;
    private String senderName;
    private String senderAddress;
    private String senderMobile;

    private Date createTime;
    private String memo;

    private static final String orderIndexPrefix = "order_";
    public String getIndexName() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createTime);
        String indexName = orderIndexPrefix + calendar.get(Calendar.YEAR);

        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }

        indexName += month;

        return indexName;

    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
