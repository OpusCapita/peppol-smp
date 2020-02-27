package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.List;

public interface ParticipantService {

    List<Participant> getAllParticipants();

    boolean saveParticipant(Participant participant);

}
