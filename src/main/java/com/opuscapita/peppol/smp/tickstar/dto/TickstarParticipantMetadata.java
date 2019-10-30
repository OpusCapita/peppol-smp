package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipantMetadata {

    private Boolean smlActivation;
    private Boolean pdActivation;
    private String registrationDate;
    private TickstarParticipantIdentifier participantIdentifier;

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

    public TickstarParticipantIdentifier getParticipantIdentifier() {
        return participantIdentifier;
    }

    public void setParticipantIdentifier(TickstarParticipantIdentifier participantIdentifier) {
        this.participantIdentifier = participantIdentifier;
    }

    public static TickstarParticipantMetadata of(Participant participant) {
        TickstarParticipantMetadata metadata = new TickstarParticipantMetadata();
        metadata.setPdActivation(true);
        metadata.setSmlActivation(true);
        metadata.setParticipantIdentifier(TickstarParticipantIdentifier.of(participant.getIcd(), participant.getIdentifier()));
        return metadata;
    }
}
