package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupDocumentType;

public class LookupDocumentDto {

    private String commonName;
    private String profileCode;
    private String localName;
    private String version;
    private String documentIdentifier;
    private String processIdentifier;
    private String url;

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public String getProcessIdentifier() {
        return processIdentifier;
    }

    public void setProcessIdentifier(String processIdentifier) {
        this.processIdentifier = processIdentifier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static LookupDocumentDto of(TickstarLookupDocumentType response) {
        if (response == null) {
            return null;
        }

        LookupDocumentDto dto = new LookupDocumentDto();
        dto.setCommonName(response.getCommonName());
        dto.setProfileCode(response.getProfileCode());
        dto.setLocalName(response.getDocumentLocalName());
        dto.setVersion(response.getVersion());
        dto.setDocumentIdentifier(response.getDocumentIdentifier());
        dto.setProcessIdentifier(response.getProcessIdentifier());
        dto.setUrl(response.getUrl());
        return dto;
    }
}
