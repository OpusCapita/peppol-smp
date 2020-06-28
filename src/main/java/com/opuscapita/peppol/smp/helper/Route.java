package com.opuscapita.peppol.smp.helper;

import com.google.gson.annotations.Since;
import com.opuscapita.peppol.smp.entity.BusinessPlatform;

import java.io.Serializable;

public class Route implements Serializable {

    @Since(1.0) private String mask;
    @Since(1.0) private BusinessPlatform platform;

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public BusinessPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(BusinessPlatform platform) {
        this.platform = platform;
    }
}