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
    public DocumentType getDocumentType(Integer externalId, SmpName smpName) {
        return getDocumentType(String.valueOf(externalId), smpName);
    }

    @Override
    public DocumentType getDocumentType(String externalId, SmpName smpName) {
        Smp smp = smpService.getSmp(smpName);
        return documentTypeRepository.findByExternalIdAndSmp(externalId, smp).stream().findFirst().orElse(null);
    }

    @Override
    public void saveDocumentType(DocumentType documentType, SmpName smpName) {
        documentType.setSmp(smpService.getSmp(smpName));
        documentTypeRepository.save(documentType);
    }
}
