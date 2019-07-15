package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Participant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParticipantServiceImpl implements ParticipantService {

    private static final Logger logger = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    private final ParticipantRepository repository;

    @Autowired
    public ParticipantServiceImpl(ParticipantRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveParticipant(Participant participant) {
        repository.save(participant);
    }

    @Override
    public Participant getParticipant(String icdIdentifier) {
        if (StringUtils.isBlank(icdIdentifier)) {
            return null;
        }

        String[] parts = icdIdentifier.split(":");
        if (parts.length != 2) {
            return null;
        }

        return getParticipant(parts[0], parts[1]);
    }

    @Override
    public Participant getParticipant(String icd, String identifier) {
        return repository.findByIdentifier(identifier).stream().filter(p -> icd.equals(p.getIcd())).findFirst().orElse(null);
    }
}
