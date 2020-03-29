package com.opuscapita.peppol.smp.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opuscapita.peppol.smp.controller.dto.DocumentTypeDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantBulkRegisterRequestDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantDto;
import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.entity.Smp;
import com.opuscapita.peppol.smp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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

        if (participantService.saveParticipantRemote(participant)) {
            participantService.saveParticipant(participant);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-participant/{id}")
    public ResponseEntity<?> deleteParticipant(@PathVariable Long id) {
        Participant participant = participantService.getParticipant(id);
        if (participantService.deleteParticipantRemote(participant)) {
            participantService.deleteParticipant(participant);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bulk-register")
    public ResponseEntity<?> bulkRegister(@RequestBody ParticipantBulkRegisterRequestDto requestDto) {
        Set<DocumentType> difiDocumentTypes = requestDto.getDocumentTypes().stream()
                .map(documentTypeDto -> documentTypeService.getDocumentTypeByInternalId(documentTypeDto.getInternalId(), SmpName.DIFI))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<DocumentType> tickstarDocumentTypes = requestDto.getDocumentTypes().stream()
                .map(documentTypeDto -> documentTypeService.getDocumentTypeByInternalId(documentTypeDto.getInternalId(), SmpName.TICKSTAR))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        String registerDate = dateFormat.format(new Date());
        Set<Participant> participants = requestDto.getParticipants().stream().map(participantDto -> {
            participantDto.setRegisteredAt(registerDate);
            Participant participant = Participant.of(participantDto);

            Smp smp = smpService.getSmpByIcd(participantDto.getIcd());
            participant.setEndpoint(endpointService.getEndpoint(smp.getName(), participantDto.getEndpointType()));
            participant.setDocumentTypes(SmpName.DIFI.equals(smp.getName()) ? difiDocumentTypes : tickstarDocumentTypes);

            return participant;
        }).collect(Collectors.toSet());

        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
        logger.info(gson.toJson(participants));

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Set<Callable<Boolean>> asyncTasks = participants.stream().map(participant -> new SaveParticipantAsyncTask(participant, participantService)).collect(Collectors.toSet());

        try {
            executor.invokeAll(asyncTasks);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }
}

class SaveParticipantAsyncTask implements Callable<Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(SaveParticipantAsyncTask.class);

    private final Participant participant;
    private final ParticipantService participantService;

    public SaveParticipantAsyncTask(Participant participant, ParticipantService participantService) {
        this.participant = participant;
        this.participantService = participantService;
    }

    @Override
    public Boolean call() {
        try {
            if (participantService.saveParticipantRemote(participant)) {
                participantService.saveParticipant(participant);
                return true;
            } else {
                logger.error(getErrorMessage());
            }
        } catch (Exception e) {
            logger.error(getErrorMessage(), e);
        }
        return false;
    }

    private String getErrorMessage() {
        return "Participant registration failed during bulk-register operation: " + participant.getIcd() + ":" + participant.getIdentifier();
    }
}
