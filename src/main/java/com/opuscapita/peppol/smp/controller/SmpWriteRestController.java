package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.controller.dto.ParticipantAddDto;
import com.opuscapita.peppol.smp.entity.Participant;
import com.opuscapita.peppol.smp.entity.Smp;
import com.opuscapita.peppol.smp.repository.DocumentTypeService;
import com.opuscapita.peppol.smp.repository.EndpointService;
import com.opuscapita.peppol.smp.repository.ParticipantService;
import com.opuscapita.peppol.smp.repository.SmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> addParticipant(@RequestBody ParticipantAddDto participantDto) {
        Participant participant = participantDto.of();
        participant.setRegisteredAt(dateFormat.format(new Date()));

        for (Long id : participantDto.getDocumentTypes()) {
            participant.getDocumentTypes().add(documentTypeService.getDocumentType(id));
        }

        Smp smp = smpService.getSmpByIcd(participant.getIcd());
        participant.setEndpoint(endpointService.getEndpoint(smp));

        if (participantService.saveParticipantRemote(participant, smp)) {
            participantService.saveParticipant(participant);
        }
        return ResponseEntity.ok().build();
    }

    private <T> ResponseEntity<T> wrap(T body) {
        if (body != null) {
            return ResponseEntity.ok(body);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
