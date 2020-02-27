package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.controller.dto.ParticipantDto;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.entity.Smp;
import com.opuscapita.peppol.smp.repository.EndpointService;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import com.opuscapita.peppol.smp.repository.SmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class SmpWriteRestController {

    private final SmpService smpService;
    private final SimpleDateFormat dateFormat;
    private final EndpointService endpointService;
    private final ParticipantService participantService;

    @Autowired
    public SmpWriteRestController(SmpService smpService, EndpointService endpointService, ParticipantService participantService) {
        this.smpService = smpService;
        this.endpointService = endpointService;
        this.participantService = participantService;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @PostMapping("/add-participant")
    public boolean addParticipant(@RequestBody ParticipantDto participantDto) {
        participantDto.setRegisteredAt(dateFormat.format(new Date()));
        Participant participant = Participant.of(participantDto);

        Smp smp = smpService.getSmpByIcd(participant.getIcd());
        participant.setEndpoint(endpointService.getEndpoint(smp.getName(), participantDto.getEndpointType()));

        return participantService.saveParticipant(participant);
    }

}
