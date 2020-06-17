package com.timing.seata.controller;


import com.timing.seata.service.impl.BusinessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    @Autowired
    private BusinessServiceImpl businessService;


    //   http://localhost:7007/buy?user=1&code=c1&count=2

    @GetMapping("/buy")
    public String buy(String user, String code, int count) {
        businessService.purchase(user, code, count);

        return "OK";
    }


    @GetMapping("/test")
    public String test() {
        businessService.test();
        return "ok";
    }
}
