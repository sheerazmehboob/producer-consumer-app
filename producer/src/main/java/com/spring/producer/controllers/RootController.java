package com.spring.producer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RootController {

    @RequestMapping(method = RequestMethod.GET)
    public String swaggerUi() {

        return "redirect:/swagger-ui/index.html";
    }

}