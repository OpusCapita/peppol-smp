package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupDocumentType;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupEndpoint;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupParticipant;

import java.util.ArrayList;
import java.util.List;

public class LookupParticipantDto {

    private String scheme;
    private String icd;
    private String identifier;

    private String apDescription;
    private String apUrl;
    private String apProtocol;
    private String apCertificate;
    private String contact;

    private List<LookupDocumentDto> documentTypes;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
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

    public String getApDescription() {
        return apDescription;
    }

    public void setApDescription(String apDescription) {
        this.apDescription = apDescription;
    }

    public String getApUrl() {
        return apUrl;
    }

    public void setApUrl(String apUrl) {
        this.apUrl = apUrl;
    }

    public String getApProtocol() {
        return apProtocol;
    }

    public void setApProtocol(String apProtocol) {
        this.apProtocol = apProtocol;
    }

    public String getApCertificate() {
        return apCertificate;
    }

    public void setApCertificate(String apCertificate) {
        this.apCertificate = apCertificate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<LookupDocumentDto> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<LookupDocumentDto> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public static LookupParticipantDto of(TickstarLookupParticipant response) {
        if (response == null) {
            return null;
        }

        LookupParticipantDto dto = new LookupParticipantDto();
        dto.setScheme(response.getIdScheme());
        dto.setIcd(response.getIdType());
        dto.setIdentifier(response.getIdValue());

        TickstarLookupEndpoint endpoint = response.getEndpoint().stream().findFirst().orElse(null);
        dto.setApDescription(endpoint.getServiceDescription());
        dto.setApUrl(endpoint.getUrl());
        dto.setApProtocol(endpoint.getTransportProfile());
        dto.setApCertificate(endpoint.getApcert());
        dto.setContact(endpoint.getTechnicalContact());

        List<LookupDocumentDto> documentTypeDtos = new ArrayList<>();
        for (TickstarLookupDocumentType documentType : endpoint.getSupportedDocuments()) {
            documentTypeDtos.add(LookupDocumentDto.of(documentType));
        }
        dto.setDocumentTypes(documentTypeDtos);
        return dto;
    }
}
