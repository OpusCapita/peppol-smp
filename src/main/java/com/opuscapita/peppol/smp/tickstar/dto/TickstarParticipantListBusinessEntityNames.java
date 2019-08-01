package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.Collections;
import java.util.List;

public class TickstarParticipantListBusinessEntityNames {

    private List<TickstarParticipantListBusinessEntityName> name;

    public List<TickstarParticipantListBusinessEntityName> getName() {
        return name;
    }

    public void setName(List<TickstarParticipantListBusinessEntityName> name) {
        this.name = name;
    }

    public static TickstarParticipantListBusinessEntityNames of(Participant participant) {
        TickstarParticipantListBusinessEntityNames businessNames = new TickstarParticipantListBusinessEntityNames();
        businessNames.setName(Collections.singletonList(TickstarParticipantListBusinessEntityName.of(participant)));
        return businessNames;
    }
}
