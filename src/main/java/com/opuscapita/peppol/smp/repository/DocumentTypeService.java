package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Smp;

import java.util.List;

public interface DocumentTypeService {

    void saveDocumentType(DocumentType documentType);

    DocumentType getDocumentType(Long id);

    DocumentType getDocumentType(Integer id, Smp smp);

    DocumentType getDocumentType(String name, Smp smp);

    List<DocumentType> getDocumentTypes(Smp smp);

}
