package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarParticipantAddContacts {

    private List<TickstarParticipantAddContact> contact;

    public List<TickstarParticipantAddContact> getContact() {
        return contact;
    }

    public void setContact(List<TickstarParticipantAddContact> contact) {
        this.contact = contact;
    }
}
