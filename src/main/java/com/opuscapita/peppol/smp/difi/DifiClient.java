package com.opuscapita.peppol.smp.difi;

import no.difi.elma.smp.webservice.Difi;
import no.difi.elma.smp.webservice.ElmaService;
import no.difi.elma.smp.webservice.responses.*;
import no.difi.elma.smp.webservice.types.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.ArrayList;
import java.util.List;

public class DifiClient extends WebServiceGatewaySupport {

    private static final String ICD_9908 = "9908";
    private static final String ICD_0192 = "0192";

    private final Difi elmaPort;
    private final String username;
    private final String password;

    public DifiClient(ElmaService elmaService, String username, String password) {
        this.username = username;
        this.password = password;
        this.elmaPort = elmaService.getElmaPort();
    }

    public GetAllParticipantsResponse getAllParticipants() {
        GetAllParticipantsType request = new GetAllParticipantsType();
        request.setUser(getAuthUser());
        return elmaPort.getAllParticipants(request);
    }

    public GetParticipantResponse getParticipant(String participantId) {
        GetParticipantType request = new GetParticipantType();
        OrganizationNumberType organizationNumber = new OrganizationNumberType();
        organizationNumber.setValue(participantId);
        request.setOrganizationNumber(organizationNumber);
        request.setUser(getAuthUser());
        return elmaPort.getParticipant(request);
    }

    public GetProfilesOnParticipantResponse getProfilesOnParticipant(String participantId) {
        GetProfilesOnParticipantType request = new GetProfilesOnParticipantType();
        OrganizationNumberType organizationNumber = new OrganizationNumberType();
        organizationNumber.setValue(participantId);
        request.setOrganizationNumber(organizationNumber);
        return elmaPort.getProfilesOnParticipant(request);
    }

    public AddParticipantResponse addParticipant(ParticipantType participant) {
        AddParticipantType request = new AddParticipantType();
        request.setParticipant(participant);
        request.setUser(getAuthUser());
        return elmaPort.addParticipant(request);
    }

    public EditParticipantResponse editParticipant(String organizationNumber, ParticipantType participant) {
        EditParticipantType editParticipantType = new EditParticipantType();
        editParticipantType.setParticipant(participant);
        OrganizationNumberType organizationNumberType = new OrganizationNumberType();
        organizationNumberType.setValue(organizationNumber);
        editParticipantType.setOrganizationNumber(organizationNumberType);
        editParticipantType.setUser(getAuthUser());
        return elmaPort.editParticipant(editParticipantType);
    }

    public DeleteParticipantResponse deleteParticipant(String icd, String identifier) {
        return deleteParticipant(icd + ":" + identifier);
    }

    public DeleteParticipantResponse deleteParticipant(String organizationNumber) {
        DeleteParticipantType deleteParticipantType = new DeleteParticipantType();
        OrganizationNumberType organizationNumberType = new OrganizationNumberType();
        organizationNumberType.setValue(organizationNumber);
        deleteParticipantType.setOrganizationNumber(organizationNumberType);
        deleteParticipantType.setUser(getAuthUser());
        return elmaPort.deleteParticipant(deleteParticipantType);
    }

    public ProfilesSupportedResponse getSupportedProfiles() {
        ProfilesSupportedType profilesSupportedType = new ProfilesSupportedType();
        return elmaPort.profilesSupported(profilesSupportedType);
    }

//    elmaPort.addProfileToAllParticipants();

//    elmaPort.removeProfileFromAllParticipants();

    public static List<String> getDifiIcd() {
        List<String> icd = new ArrayList<>();
        icd.add(DifiClient.ICD_9908);
        icd.add(DifiClient.ICD_0192);
        return icd;
    }

    public static Boolean isDifiIcd(String icd) {
        return DifiClient.ICD_9908.equals(icd) || DifiClient.ICD_0192.equals(icd);
    }

    private UserType getAuthUser() {
        UserType user = new UserType();
        UsernameType username = new UsernameType();
        username.setValue(this.username);
        user.setUsername(username);
        PasswordType password = new PasswordType();
        password.setValue(this.password);
        user.setPassword(password);
        return user;
    }
}
