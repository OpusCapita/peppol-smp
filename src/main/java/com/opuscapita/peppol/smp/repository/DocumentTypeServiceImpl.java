package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Smp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentTypeServiceImpl.class);

    private final DocumentTypeRepository repository;

    @Autowired
    public DocumentTypeServiceImpl(DocumentTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveDocumentType(DocumentType documentType) {
        repository.save(documentType);
    }

    @Override
    public DocumentType getDocumentType(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public DocumentType getDocumentType(Integer id, Smp smp) {
        return repository.findByDocumentTypeId(id).stream().filter(d -> smp.getId().equals(d.getSmp().getId())).findFirst().orElse(null);
    }

    @Override
    public DocumentType getDocumentType(String name, Smp smp) {
        return repository.findByName(name).stream().filter(d -> smp.getId().equals(d.getSmp().getId())).findFirst().orElse(null);
    }
}
