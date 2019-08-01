package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.HashSet;
import java.util.List;

public class ParticipantAddDto {

    private String icd;
    private String name;
    private String identifier;
    private String country;
    private String contactInfo;
    private List<Long> documentTypes;

    public String getIcd() {
        return icd;
    }

    public void setIcd(String icd) {
        this.icd = icd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<Long> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<Long> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Participant of() {
        Participant participant = new Participant();
        participant.setIcd(getIcd());
        participant.setName(getName());
        participant.setIdentifier(getIdentifier());
        participant.setCountry(getCountry());
        participant.setContactInfo(getContactInfo());
        participant.setDocumentTypes(new HashSet<>());
        return participant;
    }
}
