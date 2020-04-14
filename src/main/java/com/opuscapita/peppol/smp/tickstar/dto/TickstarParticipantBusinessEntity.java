package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipantBusinessEntity {

    private String countryCode;
    private String additionalInformation;
    private String geographicalInformation;
    private TickstarParticipantContacts contacts;
    private TickstarParticipantWebsiteURIs websiteURIs;
    private TickstarParticipantBusinessEntityNames names;
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

    public TickstarParticipantContacts getContacts() {
        return contacts;
    }

    public void setContacts(TickstarParticipantContacts contacts) {
        this.contacts = contacts;
    }

    public TickstarParticipantWebsiteURIs getWebsiteURIs() {
        return websiteURIs;
    }

    public void setWebsiteURIs(TickstarParticipantWebsiteURIs websiteURIs) {
        this.websiteURIs = websiteURIs;
    }

    public TickstarParticipantBusinessEntityNames getNames() {
        return names;
    }

    public void setNames(TickstarParticipantBusinessEntityNames names) {
        this.names = names;
    }

    public TickstarParticipantAddAdditionalIdentifiers getAdditionalIdentifiers() {
        return additionalIdentifiers;
    }

    public void setAdditionalIdentifiers(TickstarParticipantAddAdditionalIdentifiers additionalIdentifiers) {
        this.additionalIdentifiers = additionalIdentifiers;
    }

    public static TickstarParticipantBusinessEntity of(Participant participant) {
        TickstarParticipantBusinessEntity businessEntity = new TickstarParticipantBusinessEntity();
        businessEntity.setNames(TickstarParticipantBusinessEntityNames.of(participant));
        businessEntity.setContacts(TickstarParticipantContacts.of(participant));
        businessEntity.setCountryCode(participant.getCountry());
        return businessEntity;
    }
}
