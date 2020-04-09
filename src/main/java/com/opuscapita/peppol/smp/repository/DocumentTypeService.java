package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Smp;

import java.util.List;

public interface DocumentTypeService {

    DocumentType getDocumentType(String externalId, Smp smp);
    DocumentType getDocumentType(String externalId, SmpName smpName);

    DocumentType getDocumentType(Integer externalId, Smp smp);
    DocumentType getDocumentType(Integer externalId, SmpName smpName);

    List<DocumentType> getDocumentTypeByInternalId(Integer internalId, Smp smp);
    List<DocumentType> getDocumentTypeByInternalId(Integer internalId, SmpName smpName);

    void saveDocumentType(DocumentType documentType, Smp smp);
    void saveDocumentType(DocumentType documentType, SmpName smpName);

}
