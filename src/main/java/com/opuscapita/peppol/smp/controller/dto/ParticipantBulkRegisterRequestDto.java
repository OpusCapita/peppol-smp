package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.BusinessPlatform;

import java.util.HashSet;
import java.util.Set;

public class ParticipantBulkRegisterRequestDto {

    private Set<ParticipantDto> participants;
    private Set<DocumentTypeDto> documentTypes;
    private BusinessPlatform businessPlatform;

    public ParticipantBulkRegisterRequestDto() {
        this.participants = new HashSet<>();
        this.documentTypes = new HashSet<>();
    }

    public Set<ParticipantDto> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<ParticipantDto> participants) {
        this.participants = participants;
    }

    public Set<DocumentTypeDto> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(Set<DocumentTypeDto> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public BusinessPlatform getBusinessPlatform() {
        return businessPlatform;
    }

    public void setBusinessPlatform(BusinessPlatform businessPlatform) {
        this.businessPlatform = businessPlatform;
    }
}
