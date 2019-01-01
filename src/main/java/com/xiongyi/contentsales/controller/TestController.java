package com.xiongyi.contentsales.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiongyi on 2018/12/30.
 */
@RestController
public class TestController {

    @RequestMapping(value="/hello", method = RequestMethod.GET)
    public String sayhello() {
        return "hello xiongyi";
    }
}
