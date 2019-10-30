package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipantBusinessEntityName {

    private String name;
    private String language;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static TickstarParticipantBusinessEntityName of(Participant participant) {
        TickstarParticipantBusinessEntityName businessName = new TickstarParticipantBusinessEntityName();
        businessName.setName(participant.getName());
        return businessName;
    }
}
