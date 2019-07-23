package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.controller.dto.ParticipantDto;
import com.opuscapita.peppol.smp.controller.dto.LookupResponseDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantRequestDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantResponseDto;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import com.opuscapita.peppol.smp.tickstar.TickstarLookupClient;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SmpReadRestController {

    private final TickstarLookupClient lookupClient;
    private final ParticipantService participantService;

    @Autowired
    public SmpReadRestController(TickstarLookupClient lookupClient, ParticipantService participantService) {
        this.lookupClient = lookupClient;
        this.participantService = participantService;
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

    private <T> ResponseEntity<T> wrap(T body) {
        if (body != null) {
            return ResponseEntity.ok(body);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
