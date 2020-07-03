package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.BusinessParticipant;
import com.opuscapita.peppol.smp.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>, JpaSpecificationExecutor<Participant> {

    List<Participant> findByNameContaining(String name);

    List<Participant> findByIdentifier(String identifier);

    BusinessParticipant findByIcdAndIdentifier(String icd, String identifier);

}
