package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.difi.DifiClient;
import com.opuscapita.peppol.smp.entity.Smp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmpServiceImpl implements SmpService {

    private static final Logger logger = LoggerFactory.getLogger(SmpServiceImpl.class);

    private final SmpRepository repository;

    @Autowired
    public SmpServiceImpl(SmpRepository repository) {
        this.repository = repository;
    }

    @Override
    public Smp getSmpByIcd(String icd) {
        if (DifiClient.ICD_9908.equals(icd) || DifiClient.ICD_0192.equals(icd)) {
            return repository.findByName(SmpName.DIFI.name());
        }
        return repository.findByName(SmpName.TICKSTAR.name());
    }
}
