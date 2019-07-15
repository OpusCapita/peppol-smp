package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarMetadataListResponse {

    private List<TickstarMetadataListProfile> MetadataProfile;

    public List<TickstarMetadataListProfile> getMetadataProfile() {
        return MetadataProfile;
    }

    public void setMetadataProfile(List<TickstarMetadataListProfile> metadataProfile) {
        MetadataProfile = metadataProfile;
    }
}
