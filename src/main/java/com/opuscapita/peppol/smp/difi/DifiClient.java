package com.opuscapita.peppol.smp.difi;

import no.difi.elma.smp.webservice.ElmaService;
import no.difi.elma.smp.webservice.responses.GetAllParticipantsResponse;
import no.difi.elma.smp.webservice.types.GetAllParticipantsType;
import no.difi.elma.smp.webservice.types.PasswordType;
import no.difi.elma.smp.webservice.types.UserType;
import no.difi.elma.smp.webservice.types.UsernameType;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class DifiClient extends WebServiceGatewaySupport {

    private final String username;
    private final String password;

    public DifiClient(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public GetAllParticipantsResponse getAllParticipants() {
        ElmaService elmaService = new ElmaService();
        GetAllParticipantsType request = new GetAllParticipantsType();
        request.setUser(getAuthUser());
        return elmaService.getElmaPort().getAllParticipants(request);
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
