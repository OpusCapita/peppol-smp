package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Smp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final SmpService smpService;
    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    public DocumentTypeServiceImpl(SmpService smpService, DocumentTypeRepository documentTypeRepository) {
        this.smpService = smpService;
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public DocumentType getDocumentType(Integer externalId, Smp smp) {
        return getDocumentType(String.valueOf(externalId), smp);
    }

    @Override
    public DocumentType getDocumentType(Integer externalId, SmpName smpName) {
        return getDocumentType(String.valueOf(externalId), smpName);
    }

    @Override
    public DocumentType getDocumentType(String externalId, Smp smp) {
        return documentTypeRepository.findByExternalIdAndSmp(externalId, smp).stream().findFirst().orElse(null);
    }

    @Override
    public DocumentType getDocumentType(String externalId, SmpName smpName) {
        return getDocumentType(externalId, smpService.getSmp(smpName));
    }

    @Override
    public DocumentType getDocumentTypeByInternalId(Integer internalId, Smp smp) {
        return documentTypeRepository.findByInternalIdAndSmp(internalId, smp).stream().findFirst().orElse(null);
    }

    @Override
    public DocumentType getDocumentTypeByInternalId(Integer internalId, SmpName smpName) {
        return getDocumentTypeByInternalId(internalId, smpService.getSmp(smpName));
    }

    @Override
    public void saveDocumentType(DocumentType documentType, Smp smp) {
        documentType.setSmp(smp);
        documentTypeRepository.save(documentType);
    }

    @Override
    public void saveDocumentType(DocumentType documentType, SmpName smpName) {
        saveDocumentType(documentType, smpService.getSmp(smpName));
    }
}
