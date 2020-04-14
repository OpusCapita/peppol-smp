package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Smp;

public interface SmpService {

    Smp getSmp(SmpName smpName);

    Smp getSmpByIcd(String icd);

}
