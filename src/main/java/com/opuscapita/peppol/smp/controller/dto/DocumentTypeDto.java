package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.DocumentType;

public class DocumentTypeDto {

    private Long id;
    private Integer documentTypeId;
    private String name;
    private String profileIdentifier;
    private String documentIdentifier;
    private String smpName;

    public static DocumentTypeDto of(DocumentType documentType) {
        if (documentType == null) {
            return null;
        }

        DocumentTypeDto dto = new DocumentTypeDto();
        dto.setId(documentType.getId());
        dto.setName(documentType.getName());
        dto.setDocumentTypeId(documentType.getDocumentTypeId());
        dto.setProfileIdentifier(documentType.getProfileIdentifier());
        dto.setDocumentIdentifier(documentType.getDocumentIdentifier());
        dto.setSmpName(documentType.getSmp().getName());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Integer documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileIdentifier() {
        return profileIdentifier;
    }

    public void setProfileIdentifier(String profileIdentifier) {
        this.profileIdentifier = profileIdentifier;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public String getSmpName() {
        return smpName;
    }

    public void setSmpName(String smpName) {
        this.smpName = smpName;
    }
}
