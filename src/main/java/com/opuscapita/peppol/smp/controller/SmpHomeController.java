package com.opuscapita.peppol.smp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SmpHomeController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
