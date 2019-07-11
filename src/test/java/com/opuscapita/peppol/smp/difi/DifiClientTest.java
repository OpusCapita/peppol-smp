package com.opuscapita.peppol.smp.difi;

import com.opuscapita.peppol.smp.difi.dto.DifiParticipantBuilder;
import no.difi.elma.smp.webservice.responses.*;
import no.difi.elma.smp.webservice.types.NameType;
import no.difi.elma.smp.webservice.types.ParticipantType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class DifiClientTest {

    @Autowired
    private DifiClient difiClient;

    @Test
    public void testReadOperations() {
        GetAllParticipantsResponse response1 = difiClient.getAllParticipants();
        Assert.assertTrue(response1.getSuccess().isValue());
        Assert.assertNotNull(response1.getOrganizationNumber());
        Assert.assertFalse(response1.getOrganizationNumber().isEmpty());

        String participantId = response1.getOrganizationNumber().get(0).getValue();
        Assert.assertNotNull(participantId);

        GetParticipantResponse response2 = difiClient.getParticipant(participantId);
        Assert.assertTrue(response2.getSuccess().isValue());
        Assert.assertNotNull(response2.getParticipant());
        Assert.assertEquals(participantId, response2.getParticipant().getOrganization().getOrganizationNumber().getValue());

        GetProfilesOnParticipantResponse response3 = difiClient.getProfilesOnParticipant(participantId);
        Assert.assertTrue(response3.getSuccess().isValue());
        Assert.assertNotNull(response3.getProfiles());
        Assert.assertFalse(response3.getProfiles().isEmpty());
    }

    @Test
    public void testWriteOperations() {
        String participantId = "9908:987987988";
        String oldName = "OC Old Webservice Test";

        ParticipantType participant = new DifiParticipantBuilder()
                .setName(oldName)
                .setOrganizationNumber(participantId)
                .setContactName("Test Contact")
                .setContactEmail("test.contact@opuscapita.com")
                .setContactTelephone("212212212")
                .setWebsite("http://www.opuscapita.com")
                .addProfile("EHF_INVOICE 2.0")
                .addProfile("EHF_INVOICE_CREDITNOTE 2.0")
                .build();

        AddParticipantResponse response1 = difiClient.addParticipant(participant);
        Assert.assertTrue(response1.getSuccess().isValue());

        GetParticipantResponse response2 = difiClient.getParticipant(participantId);
        Assert.assertTrue(response2.getSuccess().isValue());
        Assert.assertEquals(response2.getParticipant().getOrganization().getName().getValue(), oldName);

        NameType newName = new NameType();
        newName.setValue("OC New Webservice Test");
        participant.getOrganization().setName(newName);
        EditParticipantResponse response3 = difiClient.editParticipant(participantId, participant);
        Assert.assertTrue(response3.getSuccess().isValue());

        GetParticipantResponse response4 = difiClient.getParticipant(participantId);
        Assert.assertTrue(response4.getSuccess().isValue());
        Assert.assertEquals(response4.getParticipant().getOrganization().getName().getValue(), newName.getValue());

        DeleteParticipantResponse response5 = difiClient.deleteParticipant(participantId);
        Assert.assertTrue(response5.getSuccess().isValue());

        GetParticipantResponse response6 = difiClient.getParticipant(participantId);
        Assert.assertFalse(response6.getSuccess().isValue());
    }
}
