package com.opuscapita.peppol.smp.tickstar;

import com.opuscapita.peppol.smp.tickstar.dto.TickstarMetadataListResponse;
import com.opuscapita.peppol.smp.tickstar.dto.TickstarParticipantListResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

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

        Set<Integer> endpoints = new HashSet<>();
        response.getParticipant().stream().forEach(p -> p.getAccessPointConfigurations().getAccessPointConfiguration().stream().forEach(a -> endpoints.add(a.getEndpointId())));
        System.out.println(endpoints.size());
    }

    @Test
    @Ignore
    public void testEndpoints() {
        String response = tickstarClient.getEndpointList();
        Assert.assertNotNull(response);
    }

    @Test
    @Ignore
    public void testMetadataList() {
        TickstarMetadataListResponse response = tickstarClient.getMetadataList();
        Assert.assertNotNull(response);
    }
}
