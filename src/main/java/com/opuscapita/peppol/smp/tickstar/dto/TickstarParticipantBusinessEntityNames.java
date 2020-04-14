package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.Collections;
import java.util.List;

public class TickstarParticipantBusinessEntityNames {

    private List<TickstarParticipantBusinessEntityName> name;

    public List<TickstarParticipantBusinessEntityName> getName() {
        return name;
    }

    public void setName(List<TickstarParticipantBusinessEntityName> name) {
        this.name = name;
    }

    public static TickstarParticipantBusinessEntityNames of(Participant participant) {
        TickstarParticipantBusinessEntityNames businessNames = new TickstarParticipantBusinessEntityNames();
        businessNames.setName(Collections.singletonList(TickstarParticipantBusinessEntityName.of(participant)));
        return businessNames;
    }
}
