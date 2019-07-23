package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.repository.EndpointType;

public class ParticipantDto {

    private Long id;
    private String icd;
    private String name;
    private String identifier;
    private String country;
    private String contactInfo;
    private String registeredAt;
    private EndpointType endpointType;
    private String smpName;

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

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
    }

    public String getSmpName() {
        return smpName;
    }

    public void setSmpName(String smpName) {
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
        dto.setContactInfo(participant.getContactInfo());
        dto.setRegisteredAt(participant.getRegisteredAt());
        dto.setEndpointType(participant.getEndpoint().getType());
        dto.setSmpName(participant.getEndpoint().getSmp().getName());
        return dto;
    }
}