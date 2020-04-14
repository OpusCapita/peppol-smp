package com.opuscapita.peppol.smp.difi.dto;

import no.difi.elma.smp.webservice.types.*;

import java.util.List;

public class DifiParticipantBuilder {

    private ParticipantType participant;

    public DifiParticipantBuilder() {
        this.participant = new ParticipantType();
        this.participant.setOrganization(new OrganizationType());
        this.participant.getOrganization().setContact(new ContactType());
    }

    public DifiParticipantBuilder setName(String name) {
        NameType nameType = new NameType();
        nameType.setValue(name);
        this.participant.getOrganization().setName(nameType);
        return this;
    }

    public DifiParticipantBuilder setOrganization(OrganizationType organization) {
        this.participant.setOrganization(organization);
        return this;
    }

    public DifiParticipantBuilder setOrganizationNumber(String organizationNumber) {
        OrganizationNumberType organizationNumberType = new OrganizationNumberType();
        organizationNumberType.setValue(organizationNumber);
        this.participant.getOrganization().setOrganizationNumber(organizationNumberType);
        return this;
    }

    public DifiParticipantBuilder setTelephone(String telephone) {
        TelephoneType telephoneType = new TelephoneType();
        telephoneType.setValue(telephone);
        this.participant.getOrganization().setTelephone(telephoneType);
        return this;
    }

    public DifiParticipantBuilder setWebsite(String website) {
        WebsiteType websiteType = new WebsiteType();
        websiteType.setValue(website);
        this.participant.getOrganization().setWebsite(websiteType);
        return this;
    }

    public DifiParticipantBuilder setContactName(String contactName) {
        NameType nameType = new NameType();
        nameType.setValue(contactName);
        this.participant.getOrganization().getContact().setName(nameType);
        return this;
    }

    public DifiParticipantBuilder setContactEmail(String contactEmail) {
        EmailType emailType = new EmailType();
        emailType.setValue(contactEmail);
        this.participant.getOrganization().getContact().setEmail(emailType);
        return this;
    }

    public DifiParticipantBuilder setContactTelephone(String contactTelephone) {
        TelephoneType telephoneType = new TelephoneType();
        telephoneType.setValue(contactTelephone);
        this.participant.getOrganization().getContact().setTelephone(telephoneType);
        return this;
    }

    public DifiParticipantBuilder addProfileType(ProfileType profileType) {
        if (this.participant.getProfiles().stream().noneMatch(p -> p.getValue().equals(profileType.getValue()))) {
            this.participant.getProfiles().add(profileType);
        }
        return this;
    }

    public DifiParticipantBuilder addProfile(String profile) {
        ProfileType profileType = new ProfileType();
        profileType.setValue(profile);
        return addProfileType(profileType);
    }

    public DifiParticipantBuilder addAllProfiles(List<String> profiles) {
        profiles.forEach(this::addProfile);
        return this;
    }

    public DifiParticipantBuilder addAllProfileTypes(List<ProfileType> profileTypes) {
        profileTypes.forEach(this::addProfileType);
        return this;
    }

    public ParticipantType build() {
        return this.participant;
    }
}
