package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.Collections;
import java.util.List;

public class TickstarParticipantAddContacts {

    private List<TickstarParticipantAddContact> contact;

    public List<TickstarParticipantAddContact> getContact() {
        return contact;
    }

    public void setContact(List<TickstarParticipantAddContact> contact) {
        this.contact = contact;
    }

    public static TickstarParticipantAddContacts of(Participant participant) {
        TickstarParticipantAddContacts businessContacts = new TickstarParticipantAddContacts();
        businessContacts.setContact(Collections.singletonList(TickstarParticipantAddContact.of(participant)));
        return businessContacts;
    }
}
