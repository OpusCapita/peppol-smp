package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.controller.dto.DocumentTypeDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantBulkRegisterRequestDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantDto;
import com.opuscapita.peppol.smp.difi.DifiScheduler;
import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.entity.Smp;
import com.opuscapita.peppol.smp.repository.*;
import com.opuscapita.peppol.smp.tickstar.TickstarScheduler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private final ParticipantRepository participantRepository;
    private final DocumentTypeService documentTypeService;

    private final DifiScheduler difiScheduler;
    private final TickstarScheduler tickstarScheduler;

    @Autowired
    public SmpWriteRestController(SmpService smpService, EndpointService endpointService,
                                  ParticipantService participantService, DocumentTypeService documentTypeService,
                                  ParticipantRepository participantRepository,
                                  DifiScheduler difiScheduler, TickstarScheduler tickstarScheduler) {
        this.smpService = smpService;
        this.endpointService = endpointService;
        this.participantService = participantService;
        this.documentTypeService = documentTypeService;
        this.participantRepository = participantRepository;
        this.difiScheduler = difiScheduler;
        this.tickstarScheduler = tickstarScheduler;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    @PostMapping("/trigger-sync-task")
    public ResponseEntity<?> triggerSyncTask() {
        new Thread(difiScheduler::updateLocalDatabase).start();
        new Thread(tickstarScheduler::updateLocalDatabase).start();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-participant/{userId}")
    public ResponseEntity<?> addParticipant(@PathVariable String userId, @RequestBody ParticipantDto participantDto) {
        Participant participant = new Participant();
        participant.copy(participantDto);
        participant.setRegisteredAt(dateFormat.format(new Date()));

        Smp smp = smpService.getSmpByIcd(participant.getIcd());
        participant.setEndpoint(endpointService.getEndpoint(smp.getName()));

        for (DocumentTypeDto documentTypeDto : participantDto.getDocumentTypes()) {
            List<DocumentType> documentTypes = documentTypeService.getDocumentTypeByInternalId(documentTypeDto.getInternalId(), smp.getName());
            participant.getDocumentTypes().addAll(documentTypes);
        }

        if (!participantService.registerParticipant(participant)) {
            return ResponseEntity.badRequest().body("Failed to save the participant");
        }

        participantService.saveParticipant(participant, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit-participant/{userId}")
    public ResponseEntity<?> editParticipant(@PathVariable String userId, @RequestBody ParticipantDto participantDto) {
        Participant participant = participantService.getParticipant(participantDto.getId());

        boolean remoteUpdateRequired = false;

        if (!StringUtils.equals(participant.getName(), participantDto.getName())) {
            participant.setName(participantDto.getName());
            remoteUpdateRequired = true;
        }

        if (!StringUtils.equals(participant.getCountry(), participantDto.getCountry())) {
            participant.setCountry(participantDto.getCountry());
            remoteUpdateRequired = true;
        }

        if (!StringUtils.equals(participant.getContactName(), participantDto.getContactName())) {
            participant.setContactName(participantDto.getContactName());
            remoteUpdateRequired = true;
        }

        if (!StringUtils.equals(participant.getContactEmail(), participantDto.getContactEmail())) {
            participant.setContactEmail(participantDto.getContactEmail());
            remoteUpdateRequired = true;
        }

        if (!StringUtils.equals(participant.getContactPhone(), participantDto.getContactPhone())) {
            participant.setContactPhone(participantDto.getContactPhone());
            remoteUpdateRequired = true;
        }

        if (!participant.getBusinessPlatform().equals(participantDto.getBusinessPlatform())) {
            participant.setBusinessPlatform(participantDto.getBusinessPlatform());
        }

        if (isDocumentTypesUpdated(participant.getDocumentTypes(), participantDto.getDocumentTypes())) {
            participant.getDocumentTypes().clear();
            Smp smp = smpService.getSmpByIcd(participant.getIcd());
            for (DocumentTypeDto documentTypeDto : participantDto.getDocumentTypes()) {
                List<DocumentType> documentTypes = documentTypeService.getDocumentTypeByInternalId(documentTypeDto.getInternalId(), smp);
                participant.getDocumentTypes().addAll(documentTypes);
            }
            remoteUpdateRequired = true;
        }

        if (remoteUpdateRequired) {
            if (!participantService.updateParticipantRegistration(participant)) {
                return ResponseEntity.badRequest().body("Failed to update the participant");
            }
        }

        participantService.saveParticipant(participant, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bulk-register/{userId}")
    public ResponseEntity<?> bulkRegister(@PathVariable String userId, @RequestBody ParticipantBulkRegisterRequestDto requestDto) {
        Set<DocumentType> difiDocumentTypes = requestDto.getDocumentTypes().stream()
                .map(documentTypeDto -> documentTypeService.getDocumentTypeByInternalId(documentTypeDto.getInternalId(), SmpName.DIFI))
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        Set<DocumentType> tickstarDocumentTypes = requestDto.getDocumentTypes().stream()
                .map(documentTypeDto -> documentTypeService.getDocumentTypeByInternalId(documentTypeDto.getInternalId(), SmpName.TICKSTAR))
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        String registerDate = dateFormat.format(new Date());
        Set<Participant> participants = requestDto.getParticipants().stream().map(participantDto -> {
            Participant participant = new Participant().copy(participantDto);
            participant.setRegisteredAt(registerDate);
            participant.setBusinessPlatform(requestDto.getBusinessPlatform());

            Smp smp = smpService.getSmpByIcd(participantDto.getIcd());
            participant.setEndpoint(endpointService.getEndpoint(smp.getName()));
            participant.setDocumentTypes(SmpName.DIFI.equals(smp.getName()) ? difiDocumentTypes : tickstarDocumentTypes);

            return participant;
        }).collect(Collectors.toSet());

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Set<Callable<Boolean>> asyncTasks = participants.stream().map(participant -> new SaveParticipantAsyncTask(userId, participant, participantService)).collect(Collectors.toSet());

        try {
            executor.invokeAll(asyncTasks);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-participant/{userId}/{id}")
    public ResponseEntity<?> deleteParticipant(@PathVariable String userId, @PathVariable Long id) {
        Participant participant = participantService.getParticipant(id);
        if (participantService.unregisterParticipant(participant)) {
            participantService.deleteParticipant(participant, userId);
        }
        return ResponseEntity.ok().build();
    }

    private boolean isDocumentTypesUpdated(Set<DocumentType> currentDocumentTypes, Set<DocumentTypeDto> newDocumentTypes) {
        if (currentDocumentTypes.size() != newDocumentTypes.size()) {
            return true;
        }
        for (DocumentType documentType : currentDocumentTypes) {
            if (newDocumentTypes.stream().noneMatch(d -> d.getInternalId().equals(documentType.getInternalId()))) {
                return true;
            }
        }
        return false;
    }
}

class SaveParticipantAsyncTask implements Callable<Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(SaveParticipantAsyncTask.class);

    private final String userId;
    private final Participant participant;
    private final ParticipantService participantService;

    public SaveParticipantAsyncTask(String userId, Participant participant, ParticipantService participantService) {
        this.userId = userId;
        this.participant = participant;
        this.participantService = participantService;
    }

    @Override
    public Boolean call() {
        try {
            if (participantService.registerParticipant(participant)) {
                participantService.saveParticipant(participant, userId);
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
