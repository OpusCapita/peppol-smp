package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.ArrayList;
import java.util.List;

public class TickstarParticipantAddWebsiteURIs {

    private List<String> websiteURI;

    public TickstarParticipantAddWebsiteURIs() {
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
