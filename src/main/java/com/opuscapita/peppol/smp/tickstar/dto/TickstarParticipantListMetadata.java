package com.opuscapita.peppol.smp.tickstar.dto;

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
}
