package com.opuscapita.peppol.smp.controller.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ParticipantRequestDto {

    private ParticipantFilterDto filter;
    private CommonPaginationDto pagination;

    public ParticipantFilterDto getFilter() {
        return filter;
    }

    public void setFilter(ParticipantFilterDto filter) {
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
