package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static TickstarParticipantListAccessPointConfigurationMetadata of(Participant participant) {
        TickstarParticipantListAccessPointConfigurationMetadata documentTypeIds = new TickstarParticipantListAccessPointConfigurationMetadata();
        documentTypeIds.setProfileId(participant.getDocumentTypes().stream().map(DocumentType::getDocumentTypeId).collect(Collectors.toList()));
        return documentTypeIds;
    }
}
