package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.OperationHistory;

import java.util.List;

public class OperationHistoryResponseDto {

    private Long totalCount;
    private List<OperationHistory> data;

    public OperationHistoryResponseDto(List<OperationHistory> operationHistory, Long totalCount) {
        this.data = operationHistory;
        this.totalCount = totalCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<OperationHistory> getData() {
        return data;
    }

    public void setData(List<OperationHistory> data) {
        this.data = data;
    }
}
