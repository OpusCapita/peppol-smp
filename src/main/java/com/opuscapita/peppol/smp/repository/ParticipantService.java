package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Participant;

public interface ParticipantService {

    void saveParticipant(Participant participant);

    Participant getParticipant(String icdIdentifier);

    Participant getParticipant(String icd, String identifier);
}
