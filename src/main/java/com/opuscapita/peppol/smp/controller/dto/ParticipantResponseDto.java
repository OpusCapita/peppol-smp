package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.List;
import java.util.stream.Collectors;

public class ParticipantResponseDto {

    private Long totalCount;
    private List<ParticipantDto> data;

    public ParticipantResponseDto(List<Participant> participantList, Long totalCount) {
        this.totalCount = totalCount;
        this.data = participantList.stream().map(ParticipantDto::of).collect(Collectors.toList());
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<ParticipantDto> getData() {
        return data;
    }

    public void setData(List<ParticipantDto> data) {
        this.data = data;
    }
}
