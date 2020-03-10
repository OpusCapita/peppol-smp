package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.controller.dto.CommonRestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SmpHomeController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping("/api/health/check")
    public CommonRestResponse health() {
        return new CommonRestResponse("Yes, I'm alive!");
    }
}
