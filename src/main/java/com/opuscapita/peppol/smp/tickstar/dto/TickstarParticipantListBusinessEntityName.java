package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipantListBusinessEntityName {

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

    public static TickstarParticipantListBusinessEntityName of(Participant participant) {
        TickstarParticipantListBusinessEntityName businessName = new TickstarParticipantListBusinessEntityName();
        businessName.setName(participant.getName());
        return businessName;
    }
}
