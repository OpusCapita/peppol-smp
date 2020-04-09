package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.OperationHistoryRequestDto;
import com.opuscapita.peppol.smp.entity.OperationHistory;
import com.opuscapita.peppol.smp.entity.OperationType;
import com.opuscapita.peppol.smp.entity.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OperationHistoryServiceImpl implements OperationHistoryService {

    private final OperationHistoryRepository repository;

    @Autowired
    public OperationHistoryServiceImpl(OperationHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<OperationHistory> getOperationHistory(OperationHistoryRequestDto request) {
        Specification<OperationHistory> spec = OperationHistoryFilterSpecification.filter(request.getFilter(), request.getPagination().getSorted());
        Pageable pageable = PageRequest.of(request.getPagination().getPage(), request.getPagination().getPageSize());
        return repository.findAll(spec, pageable);
    }

    @Override
    public void saveLog(Participant participant, String userId) {
        saveLog(participant, userId, participant.getId() == null ? OperationType.REGISTER : OperationType.UPDATE);
    }

    @Override
    public void saveLog(Participant participant, String userId, OperationType type) {
        OperationHistory history = new OperationHistory(userId, participant.getIcdIdentifier(), type);
        history.setDescription(participant.getDocumentTypes().stream().map(d -> d.getInternalId().toString()).collect(Collectors.joining(",")));
        repository.save(history);
    }

}
