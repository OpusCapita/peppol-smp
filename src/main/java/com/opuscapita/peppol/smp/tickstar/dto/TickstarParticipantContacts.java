package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.Collections;
import java.util.List;

public class TickstarParticipantContacts {

    private List<TickstarParticipantContact> contact;

    public List<TickstarParticipantContact> getContact() {
        return contact;
    }

    public void setContact(List<TickstarParticipantContact> contact) {
        this.contact = contact;
    }

    public static TickstarParticipantContacts of(Participant participant) {
        TickstarParticipantContacts businessContacts = new TickstarParticipantContacts();
        businessContacts.setContact(Collections.singletonList(TickstarParticipantContact.of(participant)));
        return businessContacts;
    }
}
