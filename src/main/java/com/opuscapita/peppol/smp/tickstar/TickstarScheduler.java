package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.repository.DocumentTypeService;
import com.opuscapita.peppol.smp.repository.SmpName;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarMetadataListProfile;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarMetadataListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TickstarScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TickstarScheduler.class);

    private TickstarClient client;
    private DocumentTypeService documentTypeService;

    @Autowired
    public TickstarScheduler(TickstarClient client, DocumentTypeService documentTypeService) {
        this.client = client;
        this.documentTypeService = documentTypeService;
    }

    //    @Scheduled(cron = "0 0 0 * * *")
    public void updateLocalDatabase() {
        logger.info("TickstarScheduler started!");
        updateDocumentTypes();
    }

    private void updateDocumentTypes() {
        logger.info("...TickstarScheduler updating document types");
        TickstarMetadataListResponse response = client.getMetadataList();

        for (TickstarMetadataListProfile tickstarMetadata : response.getMetadataProfile()) {
            DocumentType documentType = documentTypeService.getDocumentType(tickstarMetadata.getProfileId(), SmpName.TICKSTAR);
            checkDocumentType(documentType, tickstarMetadata);
        }
    }

    private void checkDocumentType(DocumentType documentType, TickstarMetadataListProfile tickstarMetadata) {
        if (documentType != null) {
            return;
        }

        logger.warn("......TickstarScheduler found a new document type, saving");
        DocumentType newDocumentType = new DocumentType();
        newDocumentType.setExternalIdAsInteger(tickstarMetadata.getProfileId());
        newDocumentType.setName(tickstarMetadata.getCommonName());

        documentTypeService.saveDocumentType(newDocumentType, SmpName.TICKSTAR);
    }
}
