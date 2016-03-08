package com.peace.twitsec.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public @ResponseBody String test() {
        return "Hello World!";
    }
}
