package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.controller.dto.ParticipantDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantRequestDto;
import com.opuscapita.peppol.smp.controller.dto.ParticipantResponseDto;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SmpReadRestController {

    private final ParticipantService participantService;

    @Autowired
    public SmpReadRestController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping("/get-participants")
    public ParticipantResponseDto getParticipants(@RequestBody ParticipantRequestDto request) {
        Page<Participant> participants = participantService.getAllParticipants(request);
        return new ParticipantResponseDto(participants.getContent(), participants.getTotalElements());
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
}
