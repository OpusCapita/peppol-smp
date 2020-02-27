package com.opuscapita.peppol.smp.difi;

import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.repository.DocumentTypeService;
import com.opuscapita.peppol.smp.repository.SmpName;
import no.difi.elma.smp.webservice.responses.ProfilesSupportedResponse;
import no.difi.elma.smp.webservice.types.CenbiiProfileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class DifiScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DifiScheduler.class);

    private DifiClient client;
    private DocumentTypeService documentTypeService;

    @Autowired
    public DifiScheduler(DifiClient client, DocumentTypeService documentTypeService) {
        this.client = client;
        this.documentTypeService = documentTypeService;
    }

    //    @Scheduled(cron = "0 0 0 * * *")
    public void updateLocalDatabase() {
        logger.info("DifiScheduler started!");
        updateDocumentTypes();
    }

    private void updateDocumentTypes() {
        logger.info("...DifiScheduler updating document types");
        ProfilesSupportedResponse response = client.getSupportedProfiles();

        for (CenbiiProfileType difiMetadata : response.getCenbiiProfiles()) {
            DocumentType documentType = documentTypeService.getDocumentType(difiMetadata.getName().getValue(), SmpName.DIFI);
            checkDocumentType(documentType, difiMetadata);
        }
    }

    private void checkDocumentType(DocumentType documentType, CenbiiProfileType difiMetadata) {
        if (documentType != null) {
            return;
        }

        logger.warn("......DifiScheduler found a new document type, saving");
        DocumentType newDocumentType = new DocumentType();
        newDocumentType.setExternalId(difiMetadata.getName().getValue());
        newDocumentType.setName(difiMetadata.getDescription().getValue());

        documentTypeService.saveDocumentType(newDocumentType, SmpName.DIFI);
    }
}
