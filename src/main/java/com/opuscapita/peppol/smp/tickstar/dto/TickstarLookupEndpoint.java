package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarLookupEndpoint {

    private String url;
    private String apcert;
    private String expiry;
    private String smpCertCN;
    private String transportProfile;
    private String technicalContact;
    private String serviceDescription;
    private String technicalInformationUrl;
    private List<TickstarLookupDocumentType> supportedDocuments;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApcert() {
        return apcert;
    }

    public void setApcert(String apcert) {
        this.apcert = apcert;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getSmpCertCN() {
        return smpCertCN;
    }

    public void setSmpCertCN(String smpCertCN) {
        this.smpCertCN = smpCertCN;
    }

    public String getTransportProfile() {
        return transportProfile;
    }

    public void setTransportProfile(String transportProfile) {
        this.transportProfile = transportProfile;
    }

    public String getTechnicalContact() {
        return technicalContact;
    }

    public void setTechnicalContact(String technicalContact) {
        this.technicalContact = technicalContact;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getTechnicalInformationUrl() {
        return technicalInformationUrl;
    }

    public void setTechnicalInformationUrl(String technicalInformationUrl) {
        this.technicalInformationUrl = technicalInformationUrl;
    }

    public List<TickstarLookupDocumentType> getSupportedDocuments() {
        return supportedDocuments;
    }

    public void setSupportedDocuments(List<TickstarLookupDocumentType> supportedDocuments) {
        this.supportedDocuments = supportedDocuments;
    }
}
