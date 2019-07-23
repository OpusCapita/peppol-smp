package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupParticipant;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarLookupResponse;

import java.util.ArrayList;
import java.util.List;

public class LookupResponseDto {

    private List<LookupParticipantDto> participants;

    public List<LookupParticipantDto> getParticipants() {
        return participants;
    }

    public void setParticipants(List<LookupParticipantDto> participants) {
        this.participants = participants;
    }

    public static LookupResponseDto of(TickstarLookupResponse response) {
        if (response == null) {
            return null;
        }

        LookupResponseDto dto = new LookupResponseDto();
        List<LookupParticipantDto> participantsDto = new ArrayList<>();
        for (TickstarLookupParticipant participant : response.getParticipants()) {
            participantsDto.add(LookupParticipantDto.of(participant));
        }
        dto.setParticipants(participantsDto);
        return dto;
    }
}
