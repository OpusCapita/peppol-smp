package com.opuscapita.peppol.smp.tickstar.dto;

public class TickstarParticipantListParticipantIdentifier {

    private String scheme;
    private String identifierCode;
    private String identifierValue;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getIdentifierCode() {
        return identifierCode;
    }

    public void setIdentifierCode(String identifierCode) {
        this.identifierCode = identifierCode;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }

    public static TickstarParticipantListParticipantIdentifier of(String icd, String identifier) {
        TickstarParticipantListParticipantIdentifier participantIdentifier = new TickstarParticipantListParticipantIdentifier();
        participantIdentifier.setScheme("iso6523-actorid-upis");
        participantIdentifier.setIdentifierCode(icd);
        participantIdentifier.setIdentifierValue(identifier);
        return participantIdentifier;
    }
}
