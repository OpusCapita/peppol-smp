package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarMetadataListResponse {

    private List<TickstarMetadataListProfile> metadataProfile;

    public List<TickstarMetadataListProfile> getMetadataProfile() {
        return metadataProfile;
    }

    public void setMetadataProfile(List<TickstarMetadataListProfile> metadataProfile) {
        this.metadataProfile = metadataProfile;
    }
}
