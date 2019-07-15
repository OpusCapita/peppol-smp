package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.entity.Smp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, Long>, JpaSpecificationExecutor<Endpoint> {

    List<Endpoint> findBySmp(Smp smp);

}
