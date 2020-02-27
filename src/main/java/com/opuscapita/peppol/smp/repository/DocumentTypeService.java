package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.DocumentType;

public interface DocumentTypeService {

    DocumentType getDocumentType(String externalId, SmpName smpName);

    DocumentType getDocumentType(Integer externalId, SmpName smpName);

    void saveDocumentType(DocumentType documentType, SmpName smpName);

}
