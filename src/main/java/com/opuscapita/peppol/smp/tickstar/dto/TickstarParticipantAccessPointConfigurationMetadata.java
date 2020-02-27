package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TickstarParticipantAccessPointConfigurationMetadata {

    private List<Integer> profileId;

    public TickstarParticipantAccessPointConfigurationMetadata() {
        this.profileId = new ArrayList<>();
    }

    public List<Integer> getProfileId() {
        return profileId;
    }

    public void setProfileId(List<Integer> profileId) {
        this.profileId = profileId;
    }

    public static TickstarParticipantAccessPointConfigurationMetadata of(Participant participant) {
        TickstarParticipantAccessPointConfigurationMetadata documentTypeIds = new TickstarParticipantAccessPointConfigurationMetadata();
        documentTypeIds.setProfileId(participant.getDocumentTypes().stream().map(DocumentType::getExternalIdAsInteger).collect(Collectors.toList()));
        return documentTypeIds;
    }
}
