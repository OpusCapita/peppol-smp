package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.controller.dto.OperationHistoryRequestDto;
import com.opuscapita.peppol.smp.entity.OperationHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

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

}
