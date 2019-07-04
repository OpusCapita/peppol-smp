package com.opuscapita.peppol.smp.tickstar.dto;

public class TickstarLookupDocumentType {

    private String url;
    private String version;
    private String commonName;
    private String profileCode;
    private String documentLocalName;
    private String documentIdentifier;
    private String processIdentifier;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

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

    public String getDocumentLocalName() {
        return documentLocalName;
    }

    public void setDocumentLocalName(String documentLocalName) {
        this.documentLocalName = documentLocalName;
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
}
