package com.timing.xmall.xorder.controller;

import com.timing.xmall.xorder.entity.Order;
import com.timing.xmall.xorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping({"/order"})
public class OrderController {

    private AtomicLong count = new AtomicLong(0);
    private Random random = new Random(10);

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public String createOrder() {
        Order order = new Order();

        order.setCreateTime(new Date());
        order.setOrderId(count.incrementAndGet());
        order.setSenderName("senderName" + count.get());
        order.setSenderAddress("senderAddress" +count.get());
        order.setSenderMobile("mobile" + count.get());

        if (random.nextInt() > 5) {
            order.setMemo("Today is " + count.get());
        } else {
            order.setMemo("Yesterday was " + count.get());
        }

        String result = orderService.addOrder(order);
        return result;

    }

    @GetMapping("/{orderId}")
    public Order getOrder(long orderId) {
        Order result = orderService.getByOrderId(orderId);
        return result;
    }
}
