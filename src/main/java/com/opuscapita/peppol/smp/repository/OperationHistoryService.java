package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.OperationHistoryRequestDto;
import com.opuscapita.peppol.smp.entity.OperationHistory;
import org.springframework.data.domain.Page;

public interface OperationHistoryService {

    Page<OperationHistory> getOperationHistory(OperationHistoryRequestDto request);

}
