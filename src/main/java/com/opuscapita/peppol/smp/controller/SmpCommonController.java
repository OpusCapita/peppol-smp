package com.opuscapita.peppol.smp.controller;

import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmpCommonController {

    @RequestMapping("/api/health/check")
    public JsonObject health() {
        JsonObject response = new JsonObject();
        response.addProperty("message", "Yes, I'm alive!");
        return response;
    }
}
