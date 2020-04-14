package com.opuscapita.peppol.smp.controller.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OperationHistoryRequestDto {

    private OperationHistoryFilterDto filter;
    private CommonPaginationDto pagination;

    public OperationHistoryFilterDto getFilter() {
        return filter;
    }

    public void setFilter(OperationHistoryFilterDto filter) {
        this.filter = filter;
    }

    public CommonPaginationDto getPagination() {
        return pagination;
    }

    public void setPagination(CommonPaginationDto pagination) {
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
