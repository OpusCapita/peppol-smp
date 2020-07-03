package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.controller.dto.*;
import com.opuscapita.peppol.smp.entity.BusinessPlatform;
import com.opuscapita.peppol.smp.entity.OperationHistory;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.helper.BusinessPlatformDefiner;
import com.opuscapita.peppol.smp.repository.OperationHistoryService;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SmpReadRestController {

    private static final Logger logger = LoggerFactory.getLogger(SmpReadRestController.class);

    private final ParticipantService participantService;
    private final BusinessPlatformDefiner businessPlatformDefiner;
    private final OperationHistoryService operationHistoryService;

    @Autowired
    public SmpReadRestController(ParticipantService participantService, BusinessPlatformDefiner businessPlatformDefiner,
                                 OperationHistoryService operationHistoryService) {
        this.participantService = participantService;
        this.businessPlatformDefiner = businessPlatformDefiner;
        this.operationHistoryService = operationHistoryService;
    }

    @PostMapping("/get-participants")
    public ParticipantResponseDto getParticipants(@RequestBody ParticipantRequestDto request) {
        Page<Participant> participants = participantService.getAllParticipants(request);
        return new ParticipantResponseDto(participants.getContent(), participants.getTotalElements());
    }

    @PostMapping("/get-operation-history")
    public OperationHistoryResponseDto getOperationHistory(@RequestBody OperationHistoryRequestDto request) {
        Page<OperationHistory> participants = operationHistoryService.getOperationHistory(request);
        return new OperationHistoryResponseDto(participants.getContent(), participants.getTotalElements());
    }

    @GetMapping("/get-participant/{icd}/{identifier}")
    public ParticipantDto getParticipant(@PathVariable String icd, @PathVariable String identifier) {
        Participant participant = participantService.getParticipant(icd, identifier);
        return ParticipantDto.of(participant);
    }

    @GetMapping("/get-participant-by-id/{id}")
    public ParticipantDto getParticipantById(@PathVariable Long id) {
        Participant participant = participantService.getParticipant(id);
        return ParticipantDto.of(participant);
    }

    @GetMapping("/get-business-platform/{icd}/{identifier}")
    public BusinessPlatform getBusinessPlatform(@PathVariable String icd, @PathVariable String identifier) {
        BusinessPlatform businessPlatform = participantService.getBusinessPlatform(icd, identifier);
        if (businessPlatform == null) {
            logger.warn("This SHOULD NOT happen, no route info for participant: " + icd + ":" + identifier);
            businessPlatform = businessPlatformDefiner.define(icd, identifier);
        }
        return businessPlatform;
    }
}
