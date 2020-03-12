package com.opuscapita.peppol.smp.difi;

import com.opuscapita.peppol.smp.difi.dto.DifiParticipantBuilder;
import no.difi.elma.smp.webservice.responses.*;
import no.difi.elma.smp.webservice.types.ElmaBasicType;
import no.difi.elma.smp.webservice.types.NameType;
import no.difi.elma.smp.webservice.types.ParticipantType;
import org.apache.poi.ss.usermodel.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class DifiClientTest {

    @Autowired
    private DifiClient difiClient;

    private DecimalFormat dc = new DecimalFormat("#");

    @Test
    @Ignore
    public void readFromExcelAndCreateNew() {
        String filename = "/test-material/ELMA-registrering-bonitas.xlsx";
        File file = new File(getClass().getResource(filename).getFile());
        try (InputStream inputStream = new FileInputStream(file)) {

            Workbook wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(0);

            for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(1);

                if (cell == null) {
                    continue;
                }

                String participantId = CellType.NUMERIC.equals(cell.getCellType()) ?
                        dc.format(cell.getNumericCellValue()) :
                        cell.getStringCellValue().replaceAll("\\s", "");

                GetParticipantResponse getParticipantResponse = difiClient.getParticipant(participantId);
                if (getParticipantResponse.getParticipant() != null) {
                    System.out.println(i + ". Participant: " + participantId + " is registered, ignoring...");
                } else {
                    System.out.println(i + ". Participant: " + participantId + " is NOT registered, creating...");

                    ParticipantType newParticipant = buildFromRow(row, participantId);
                    AddParticipantResponse response = difiClient.addParticipant(newParticipant);
                    if (response.getSuccess().isValue()) {
                        System.out.println("     Created!");
                    } else {
                        System.out.println("     Error occurred: " + response.getErrorMessages().stream().map(ElmaBasicType::getValue).collect(Collectors.joining(", ")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ParticipantType buildFromRow(Row row, String participantId) {
        return new DifiParticipantBuilder()
                .setOrganizationNumber(participantId)
                .setName(row.getCell(0).getStringCellValue())
                .setContactName(row.getCell(2).getStringCellValue())
                .setContactTelephone(row.getCell(3).getStringCellValue())
                .setContactEmail(row.getCell(4).getStringCellValue())
                .addProfile("EHF_CREDITNOTE 2.0")
                .addProfile("PEPPOLBIS_3_0_BILLING_01_UBL")
                .addProfile("EHF_INVOICE 2.0")
                .addProfile("BIS04 V2")
                .addProfile("EHF_INVOICE_CREDITNOTE 2.0")
                .addProfile("BIS05 V2")
                .build();
    }

    @Test
    @Ignore
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
    }

    @Test
    @Ignore
    public void testGetSupportedProfiles() {
        ProfilesSupportedResponse response = difiClient.getSupportedProfiles();
        Assert.assertNotNull(response.getCenbiiProfiles());
        Assert.assertFalse(response.getCenbiiProfiles().isEmpty());
    }

    @Test
    @Ignore
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
