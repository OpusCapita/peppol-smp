package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipantContact {

    private String name;
    private String type;
    private String email;
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static TickstarParticipantContact of(Participant participant) {
        TickstarParticipantContact businessContact = new TickstarParticipantContact();
        businessContact.setName(participant.getContactName());
        businessContact.setEmail(participant.getContactEmail());
        businessContact.setPhoneNumber(participant.getContactPhone());
        return businessContact;
    }
}
