package com.opuscapita.peppol.smp.controller;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SmpHomeController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping("/api/health/check")
    public JsonObject health() {
        JsonObject response = new JsonObject();
        response.addProperty("message", "Yes, I'm alive!");
        return response;
    }
}
