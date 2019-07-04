package com.opuscapita.peppol.smp.tickstar.dto;

public class TickstarParticipantListBusinessEntity {

    private String countryCode;
    private TickstarParticipantListBusinessEntityNames names;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public TickstarParticipantListBusinessEntityNames getNames() {
        return names;
    }

    public void setNames(TickstarParticipantListBusinessEntityNames names) {
        this.names = names;
    }
}
