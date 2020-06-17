package com.timing.modules.sys.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/role")
@Api("role admin manager")
public class RoleController {

    @GetMapping("/list")
    @ApiOperation("get role list")
    public String list() {



        return "aa";
    }
}
