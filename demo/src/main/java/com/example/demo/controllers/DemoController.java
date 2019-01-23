package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DemoController {

    @Value("${spring.application.name}")
    String appName;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String namePage(Model model, @PathVariable String name) {
        model.addAttribute("name", name);
        model.addAttribute("appName", appName);
        return "name";
    }
}
