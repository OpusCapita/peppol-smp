package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Smp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SmpRepository extends JpaRepository<Smp, Long>, JpaSpecificationExecutor<Smp> {

    Smp findByName(String name);

}
