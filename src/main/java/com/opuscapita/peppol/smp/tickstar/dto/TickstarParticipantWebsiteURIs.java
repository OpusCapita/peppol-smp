package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.ArrayList;
import java.util.List;

public class TickstarParticipantWebsiteURIs {

    private List<String> websiteURI;

    public TickstarParticipantWebsiteURIs() {
        this.websiteURI = new ArrayList<>();
    }

    public List<String> getWebsiteURI() {
        return websiteURI;
    }

    public void setWebsiteURI(List<String> websiteURI) {
        this.websiteURI = websiteURI;
    }

    public void addWebsiteURI(String websiteURI) {
        this.websiteURI.add(websiteURI);
    }
}
