package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.ParticipantRequestDto;
import com.opuscapita.peppol.smp.entity.BusinessPlatform;
import com.opuscapita.peppol.smp.entity.Participant;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ParticipantService {

    // gets participant by id from database
    Participant getParticipant(Long id);

    // gets participant by icdIdentifier (ex: 0192:12345678) from database
    Participant getParticipant(String icdIdentifier);

    // gets participant by icd and identifier from database
    Participant getParticipant(String icd, String identifier);

    // lists all participants by given filtering and paging options
    Page<Participant> getAllParticipants(ParticipantRequestDto request);

    // gets business platform of the participant
    BusinessPlatform getBusinessPlatform(String icd, String identifier);

    // saves participant entity to database
    void saveParticipant(Participant participant, String userId);

    // removes participant entity from database
    void deleteParticipant(Participant participant, String userId);

    // registers participant to appropriate smp
    boolean registerParticipant(Participant participant);

    // updates participant registration in appropriate smp
    boolean updateParticipantRegistration(Participant participant);

    // removes participant from appropriate smp
    boolean unregisterParticipant(Participant participant);
}
