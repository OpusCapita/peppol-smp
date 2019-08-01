package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipantListMetadata {

    private Boolean smlActivation;
    private Boolean pdActivation;
    private String registrationDate;
    private TickstarParticipantListParticipantIdentifier participantIdentifier;

    public Boolean getSmlActivation() {
        return smlActivation;
    }

    public void setSmlActivation(Boolean smlActivation) {
        this.smlActivation = smlActivation;
    }

    public Boolean getPdActivation() {
        return pdActivation;
    }

    public void setPdActivation(Boolean pdActivation) {
        this.pdActivation = pdActivation;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public TickstarParticipantListParticipantIdentifier getParticipantIdentifier() {
        return participantIdentifier;
    }

    public void setParticipantIdentifier(TickstarParticipantListParticipantIdentifier participantIdentifier) {
        this.participantIdentifier = participantIdentifier;
    }

    public static TickstarParticipantListMetadata of(Participant participant) {
        TickstarParticipantListMetadata metadata = new TickstarParticipantListMetadata();
        metadata.setPdActivation(true);
        metadata.setSmlActivation(true);
        metadata.setParticipantIdentifier(TickstarParticipantListParticipantIdentifier.of(participant.getIcd(), participant.getIdentifier()));
        return metadata;
    }
}
