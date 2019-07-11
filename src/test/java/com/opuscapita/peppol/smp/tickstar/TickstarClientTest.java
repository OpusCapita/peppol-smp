package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantListResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class TickstarClientTest {

    @Autowired
    private TickstarClient tickstarClient;

    @Test
    @Ignore
    public void testConnection() {
        TickstarParticipantListResponse response = tickstarClient.getAllParticipants();
        Assert.assertNotNull(response);
    }
}
