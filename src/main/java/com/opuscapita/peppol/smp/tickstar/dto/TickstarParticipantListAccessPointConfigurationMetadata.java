package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.ArrayList;
import java.util.List;

public class TickstarParticipantListAccessPointConfigurationMetadata {

    private List<Integer> profileId;

    public TickstarParticipantListAccessPointConfigurationMetadata() {
        this.profileId = new ArrayList<>();
    }

    public List<Integer> getProfileId() {
        return profileId;
    }

    public void setProfileId(List<Integer> profileId) {
        this.profileId = profileId;
    }
}
