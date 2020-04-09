package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.repository.EndpointType;
import com.opuscapita.peppol.smp.repository.SmpName;

import java.util.Set;

public class ParticipantDto {

    private Long id;
    private String icd;
    private String name;
    private String identifier;
    private String country;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String registeredAt;
    private EndpointType endpointType;
    private Set<DocumentTypeDto> documentTypes;

    private SmpName smpName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
    }

    public Set<DocumentTypeDto> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(Set<DocumentTypeDto> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public SmpName getSmpName() {
        return smpName;
    }

    public void setSmpName(SmpName smpName) {
        this.smpName = smpName;
    }

    public static ParticipantDto of(Participant participant) {
        if (participant == null) {
            return null;
        }

        ParticipantDto dto = new ParticipantDto();
        dto.setId(participant.getId());
        dto.setIcd(participant.getIcd());
        dto.setName(participant.getName());
        dto.setIdentifier(participant.getIdentifier());
        dto.setCountry(participant.getCountry());
        dto.setContactName(participant.getContactName());
        dto.setContactEmail(participant.getContactEmail());
        dto.setContactPhone(participant.getContactPhone());
        dto.setRegisteredAt(participant.getRegisteredAt());
        dto.setEndpointType(participant.getEndpoint().getType());
        dto.setDocumentTypes(DocumentTypeDto.of(participant.getDocumentTypes()));
        dto.setSmpName(participant.getEndpoint().getSmp().getName());
        return dto;
    }
}
