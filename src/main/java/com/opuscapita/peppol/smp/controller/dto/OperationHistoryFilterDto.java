package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.OperationType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;

public class OperationHistoryFilterDto {

    private String user;
    private String participant;
    private Date startDate;
    private Date endDate;
    private List<OperationType> types;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<OperationType> getTypes() {
        return types;
    }

    public void setTypes(List<OperationType> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
