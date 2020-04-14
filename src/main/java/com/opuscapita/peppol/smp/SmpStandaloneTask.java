package com.opuscapita.peppol.smp;

import com.opuscapita.peppol.smp.difi.DifiClient;
import com.opuscapita.peppol.smp.difi.dto.DifiParticipantBuilder;
import com.opuscapita.peppol.smp.tickstar.TickstarClient;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipant;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantAccessPointConfiguration;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantIdentifier;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantListResponse;
import no.difi.elma.smp.webservice.responses.EditParticipantResponse;
import no.difi.elma.smp.webservice.responses.GetAllParticipantsResponse;
import no.difi.elma.smp.webservice.responses.GetParticipantResponse;
import no.difi.elma.smp.webservice.types.ElmaBasicType;
import no.difi.elma.smp.webservice.types.OrganizationNumberType;
import no.difi.elma.smp.webservice.types.ParticipantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Component
public class SmpStandaloneTask implements CommandLineRunner {

    private static final boolean IGNORE = true;

    @Autowired
    private DifiClient difiClient;

    @Autowired
    private TickstarClient tickstarClient;

    @Override
    public void run(String... args) throws Exception {
        if (IGNORE) {
            System.out.println("Skipping standalone task runner...");
            return;
        }

//        updateDifi();
//        updateTickstar();
    }

    private void updateDifi() throws Exception {
        System.out.println("STARTED for DIFI!");

        List<String> logs = new ArrayList<>();
        GetAllParticipantsResponse listResponse = difiClient.getAllParticipants();
        for (OrganizationNumberType organizationNumberType : listResponse.getOrganizationNumber()) {
            updateOrganizationWithBISv3TypeDifi(organizationNumberType.getValue(), "PEPPOLBIS_3_0_BILLING_01_UBL", logs);
        }

        System.out.println("FINISHED for DIFI!");

        Files.write(Paths.get("C:\\Users\\ibilge\\IdeaProjects\\peppol-smp\\src\\main\\java\\com\\opuscapita\\peppol\\smp\\difi-logs.txt"), logs, StandardCharsets.UTF_8);
    }

    private void updateTickstar() throws Exception {
        System.out.println("STARTED for Tickstar!");

        List<Integer> profilesToAdd = new ArrayList<>();
        profilesToAdd.add(158);
        profilesToAdd.add(160);
//        profilesToAdd.add(172);
//        profilesToAdd.add(173);
//        profilesToAdd.add(174);
//        profilesToAdd.add(175);
//        profilesToAdd.add(176);
//        profilesToAdd.add(177);
//        profilesToAdd.add(179);
//        profilesToAdd.add(180);
//        profilesToAdd.add(181);


        List<String> logs = new ArrayList<>();
        TickstarParticipantListResponse listResponse = tickstarClient.getAllParticipants();
        for (TickstarParticipant participant : listResponse.getParticipant()) {
            updateOrganizationWithBISv3TypeTickstar(participant, profilesToAdd, logs);
        }

        System.out.println("FINISHED for Tickstar!");

        Files.write(Paths.get("C:\\Users\\ibilge\\IdeaProjects\\peppol-smp\\src\\main\\java\\com\\opuscapita\\peppol\\smp\\tickstar-logs.txt"), logs, StandardCharsets.UTF_8);
    }

    private void updateOrganizationWithBISv3TypeDifi(String organizationNumber, String profileToAdd, List<String> logs) {
        String icd = "9908";
        GetParticipantResponse getParticipantResponse = difiClient.getParticipant(icd + ":" + organizationNumber);

        if (getParticipantResponse.getParticipant() != null) {
            return; // skip...
        }

        logs.add("Running for " + organizationNumber);
        System.out.println("Running for " + organizationNumber);
        getParticipantResponse = difiClient.getParticipant(icd + ":" + organizationNumber);

        ParticipantType updatedParticipant = new DifiParticipantBuilder()
                .setOrganizationNumber(icd + ":" + organizationNumber)
                .setOrganization(getParticipantResponse.getParticipant().getOrganization())
                .addAllProfileTypes(getParticipantResponse.getParticipant().getProfiles())
                .addProfile(profileToAdd)
                .build();

        EditParticipantResponse editParticipantResponse = difiClient.editParticipant(organizationNumber, updatedParticipant);
        if (editParticipantResponse.getSuccess().isValue()) {
            logs.add("      Created new organization for 0192:" + organizationNumber);
            System.out.println("      Created new organization for 0192:" + organizationNumber);
        } else {
            logs.add("      Failed new organization for 0192:" + organizationNumber + ", reason: " + editParticipantResponse.getErrorMessages().stream().map(ElmaBasicType::getValue).collect(Collectors.joining(",")));
            System.out.println("      Failed new organization for 0192:" + organizationNumber + ", reason: " + editParticipantResponse.getErrorMessages().stream().map(ElmaBasicType::getValue).collect(Collectors.joining(",")));
        }
    }

    private void updateOrganizationWithBISv3TypeTickstar(TickstarParticipant participant, List<Integer> profilesToAdd, List<String> logs) {
        TickstarParticipantAccessPointConfiguration apConfig = participant.getAccessPointConfigurations().getAccessPointConfiguration().stream().filter(ap -> 100 == ap.getEndpointId()).findFirst().orElse(null);
        if (apConfig == null) {
            return;
        }

        TickstarParticipantIdentifier identifierObj = participant.getMeta().getParticipantIdentifier();
        String identifier = identifierObj.getIdentifierCode() + ":" + identifierObj.getIdentifierValue();

        List<Integer> currentProfiles = apConfig.getMetadataProfileIds().getProfileId();
        List<Integer> missingProfiles = profilesToAdd.stream().filter(p -> !currentProfiles.contains(p)).collect(Collectors.toList());
        if (missingProfiles.isEmpty()) {
            return;
        }

        apConfig.getMetadataProfileIds().getProfileId().addAll(missingProfiles);
        HttpStatus responseStatus = tickstarClient.editParticipant(participant);

        System.out.println("Running for " + identifier);
        logs.add("Running for " + identifier + ", adding missing profiles [" + missingProfiles.stream().map(Object::toString).collect(Collectors.joining(", ")) + "], result: " + responseStatus);
    }
}
