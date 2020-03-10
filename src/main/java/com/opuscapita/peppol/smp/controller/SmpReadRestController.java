package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.controller.dto.ParticipantDto;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SmpReadRestController {

    private final ParticipantService participantService;

    @Autowired
    public SmpReadRestController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping("/get-participants")
    public List<ParticipantDto> getParticipants() {
        return participantService.getAllParticipants().stream().map(ParticipantDto::of).collect(Collectors.toList());
    }


}
