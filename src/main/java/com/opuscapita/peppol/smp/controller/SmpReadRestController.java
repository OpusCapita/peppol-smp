package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.controller.dto.*;
import com.opuscapita.peppol.smp.entity.DocumentType;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.entity.Smp;
import com.opuscapita.peppol.smp.repository.DocumentTypeService;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import com.opuscapita.peppol.smp.repository.SmpService;
import com.opuscapita.peppol.smp.tickstar.TickstarLookupClient;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SmpReadRestController {

    private final SmpService smpService;
    private final TickstarLookupClient lookupClient;
    private final ParticipantService participantService;
    private final DocumentTypeService documentTypeService;

    @Autowired
    public SmpReadRestController(SmpService smpService, TickstarLookupClient lookupClient,
                                 ParticipantService participantService, DocumentTypeService documentTypeService) {
        this.smpService = smpService;
        this.lookupClient = lookupClient;
        this.participantService = participantService;
        this.documentTypeService = documentTypeService;
    }

    @PostMapping("/get-participants")
    public ParticipantResponseDto getParticipants(@RequestBody ParticipantRequestDto request) {
        Page<Participant> participants = participantService.getAllParticipants(request);
        return new ParticipantResponseDto(participants.getContent(), participants.getTotalElements());
    }

    @GetMapping("/get-participant-by-id/{id}")
    public ResponseEntity<?> getParticipantById(@PathVariable Long id) {
        Participant participant = participantService.getParticipant(id);
        return wrap(ParticipantDto.of(participant));
    }

    @GetMapping("/lookup/{icd}/{identifier}")
    public LookupResponseDto getLookup(@PathVariable String icd, @PathVariable String identifier) {
        TickstarLookupResponse response = lookupClient.lookup(icd, identifier);
        return LookupResponseDto.of(response);
    }

    @GetMapping("/get-document-types/{icd}")
    public List<DocumentTypeDto> getDocumentTypes(@PathVariable String icd) {
        Smp smp = smpService.getSmpByIcd(icd);
        List<DocumentType> documentTypes = documentTypeService.getDocumentTypes(smp);
        return documentTypes.stream().map(DocumentTypeDto::of).collect(Collectors.toList());
    }

    private <T> ResponseEntity<T> wrap(T body) {
        if (body != null) {
            return ResponseEntity.ok(body);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
