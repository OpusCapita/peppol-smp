package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.OperationHistoryRequestDto;
import com.opuscapita.peppol.smp.entity.OperationHistory;
import com.opuscapita.peppol.smp.entity.OperationType;
import com.opuscapita.peppol.smp.entity.Participant;
import org.springframework.data.domain.Page;

public interface OperationHistoryService {

    Page<OperationHistory> getOperationHistory(OperationHistoryRequestDto request);

    void saveLog(Participant participant, String userId);

    void saveLog(Participant participant, String userId, OperationType type);

}
