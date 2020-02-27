package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.DocumentType;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentTypeDto {

    private String name;
    private Integer internalId;
    private String externalId;

    public static Set<DocumentTypeDto> of(Set<DocumentType> documentTypes) {
        if (documentTypes == null || documentTypes.isEmpty()) {
            return new HashSet<>();
        }
        return documentTypes.stream().map(DocumentTypeDto::of).collect(Collectors.toSet());
    }

    public static DocumentTypeDto of(DocumentType documentType) {
        if (documentType == null) {
            return null;
        }

        DocumentTypeDto dto = new DocumentTypeDto();
        dto.setName(documentType.getName());
        dto.setInternalId(documentType.getInternalId());
        dto.setExternalId(documentType.getExternalId());
        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInternalId() {
        return internalId;
    }

    public void setInternalId(Integer internalId) {
        this.internalId = internalId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
