package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipantAddBusinessEntity {

    private String countryCode;
    private String additionalInformation;
    private String geographicalInformation;
    private TickstarParticipantAddContacts contacts;
    private TickstarParticipantAddWebsiteURIs websiteURIs;
    private TickstarParticipantListBusinessEntityNames names;
    private TickstarParticipantAddAdditionalIdentifiers additionalIdentifiers;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getGeographicalInformation() {
        return geographicalInformation;
    }

    public void setGeographicalInformation(String geographicalInformation) {
        this.geographicalInformation = geographicalInformation;
    }

    public TickstarParticipantAddContacts getContacts() {
        return contacts;
    }

    public void setContacts(TickstarParticipantAddContacts contacts) {
        this.contacts = contacts;
    }

    public TickstarParticipantAddWebsiteURIs getWebsiteURIs() {
        return websiteURIs;
    }

    public void setWebsiteURIs(TickstarParticipantAddWebsiteURIs websiteURIs) {
        this.websiteURIs = websiteURIs;
    }

    public TickstarParticipantListBusinessEntityNames getNames() {
        return names;
    }

    public void setNames(TickstarParticipantListBusinessEntityNames names) {
        this.names = names;
    }

    public TickstarParticipantAddAdditionalIdentifiers getAdditionalIdentifiers() {
        return additionalIdentifiers;
    }

    public void setAdditionalIdentifiers(TickstarParticipantAddAdditionalIdentifiers additionalIdentifiers) {
        this.additionalIdentifiers = additionalIdentifiers;
    }

    public static TickstarParticipantAddBusinessEntity of(Participant participant) {
        TickstarParticipantAddBusinessEntity businessEntity = new TickstarParticipantAddBusinessEntity();
        businessEntity.setNames(TickstarParticipantListBusinessEntityNames.of(participant));
        businessEntity.setContacts(TickstarParticipantAddContacts.of(participant));
        businessEntity.setCountryCode(participant.getCountry());
        return businessEntity;
    }
}
