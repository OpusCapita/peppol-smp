package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.difi.DifiClient;
import com.opuscapita.peppol.smp.entity.Smp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmpServiceImpl implements SmpService {

    private final SmpRepository repository;

    @Autowired
    public SmpServiceImpl(SmpRepository repository) {
        this.repository = repository;
    }

    @Override
    public Smp getSmp(SmpName smpName) {
        return repository.findByName(smpName);
    }

    @Override
    public Smp getSmpByIcd(String icd) {
        return DifiClient.isDifiIcd(icd) ? repository.findByName(SmpName.DIFI) : repository.findByName(SmpName.TICKSTAR);
    }
}
