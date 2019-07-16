package com.opuscapita.peppol.smp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmpCommonController {

    @RequestMapping("/api/health/check")
    public CommonRestResponse health() {
        return new CommonRestResponse("Yes, I'm alive!");
    }
}
