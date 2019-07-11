package com.opuscapita.peppol.smp.tickstar.dto;

public class TickstarMetadataListProfile {

    private String commonName;
    private Integer profileId;
    private TickstarMetadataListConstraints constraints;
    private TickstarMetadataListProcessIdentifier processIdentifier;
    private TickstarMetadataListDocumentIdentifier documentIdentifier;

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public TickstarMetadataListConstraints getConstraints() {
        return constraints;
    }

    public void setConstraints(TickstarMetadataListConstraints constraints) {
        this.constraints = constraints;
    }

    public TickstarMetadataListProcessIdentifier getProcessIdentifier() {
        return processIdentifier;
    }

    public void setProcessIdentifier(TickstarMetadataListProcessIdentifier processIdentifier) {
        this.processIdentifier = processIdentifier;
    }

    public TickstarMetadataListDocumentIdentifier getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(TickstarMetadataListDocumentIdentifier documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }
}
