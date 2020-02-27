package com.opuscapita.peppol.smp.entity;

import com.opuscapita.peppol.smp.controller.dto.ParticipantDto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Participant {

    private String name;
    private String icd;
    private String identifier;
    private String country;
    private String contactInfo;
    private String registeredAt;
    private Set<DocumentType> documentTypes;

    private Endpoint endpoint;

    public Participant() {
        this.documentTypes = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcd() {
        return icd;
    }

    public void setIcd(String icd) {
        this.icd = icd;
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

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Set<DocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(Set<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public static Participant of(ParticipantDto participantDto) {
        Participant participant = new Participant();

        participant.setIcd(participantDto.getIcd());
        participant.setName(participantDto.getName());
        participant.setIdentifier(participantDto.getIdentifier());
        participant.setCountry(participantDto.getCountry());
        participant.setContactInfo(participantDto.getContactInfo());
        participant.setRegisteredAt(participantDto.getRegisteredAt());
        participant.setDocumentTypes(DocumentType.of(participantDto.getDocumentTypes()));

        return participant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return icd.equals(that.icd) && identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icd, identifier);
    }
}
