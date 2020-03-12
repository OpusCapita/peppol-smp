package com.opuscapita.peppol.smp.controller.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ParticipantRequestDto {

    private ParticipantFilterDto filter;
    private ParticipantPaginationDto pagination;

    public ParticipantFilterDto getFilter() {
        return filter;
    }

    public void setFilter(ParticipantFilterDto filter) {
        this.filter = filter;
    }

    public ParticipantPaginationDto getPagination() {
        return pagination;
    }

    public void setPagination(ParticipantPaginationDto pagination) {
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
