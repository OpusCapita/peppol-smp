package com.opuscapita.peppol.smp.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opuscapita.peppol.smp.controller.dto.DocumentTypeDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantDto;
import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.entity.Smp;
import com.opuscapita.peppol.smp.repository.DocumentTypeService;
import com.opuscapita.peppol.smp.repository.EndpointService;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import com.opuscapita.peppol.smp.repository.SmpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class SmpWriteRestController {

    private static final Logger logger = LoggerFactory.getLogger(SmpWriteRestController.class);

    private final SmpService smpService;
    private final SimpleDateFormat dateFormat;
    private final EndpointService endpointService;
    private final ParticipantService participantService;
    private final DocumentTypeService documentTypeService;

    @Autowired
    public SmpWriteRestController(SmpService smpService, EndpointService endpointService,
                                  ParticipantService participantService, DocumentTypeService documentTypeService) {
        this.smpService = smpService;
        this.endpointService = endpointService;
        this.participantService = participantService;
        this.documentTypeService = documentTypeService;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @PostMapping("/add-participant")
    public ResponseEntity<?> addParticipant(@RequestBody ParticipantDto participantDto) {
        participantDto.setRegisteredAt(dateFormat.format(new Date()));
        Participant participant = Participant.of(participantDto);

        Smp smp = smpService.getSmpByIcd(participant.getIcd());
        participant.setEndpoint(endpointService.getEndpoint(smp.getName(), participantDto.getEndpointType()));

        for (DocumentTypeDto documentTypeDto : participantDto.getDocumentTypes()) {
            DocumentType documentType = documentTypeService.getDocumentTypeByInternalId(documentTypeDto.getInternalId(), smp.getName());
            if (documentType != null) {
                participant.getDocumentTypes().add(documentType);
            }
        }

        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
        logger.info(gson.toJson(participant));

//        if (participantService.saveParticipantRemote(participant)) {
//            participantService.saveParticipant(participant);
//        }
        return ResponseEntity.ok().build();
    }

}
