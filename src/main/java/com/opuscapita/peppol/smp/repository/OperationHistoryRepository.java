package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.OperationHistory;
import com.opuscapita.peppol.smp.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationHistoryRepository extends JpaRepository<OperationHistory, Long>, JpaSpecificationExecutor<OperationHistory> {

    List<OperationHistory> findByUser(String user);

    List<OperationHistory> findByType(OperationType type);

    List<OperationHistory> findByParticipant(String participant);

}
