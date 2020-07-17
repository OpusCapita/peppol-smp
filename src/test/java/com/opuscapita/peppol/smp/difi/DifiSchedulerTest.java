package com.opuscapita.peppol.smp.difi;

import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.repository.EndpointService;
import com.opuscapita.peppol.smp.repository.SmpName;
import org.junit.Before;
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
public class DifiSchedulerTest {

    @Autowired
    private DifiScheduler scheduler;

    @Autowired
    private EndpointService endpointService;

    private Endpoint endpoint;

    @Before
    public void setUp() {
        endpoint = endpointService.getEndpoint(SmpName.DIFI);
    }

    @Test
    @Ignore
    public void testScheduler() {
        scheduler.updateLocalDatabase();
    }

    @Test
    @Ignore
    public void updateParticipants() {
        String[] participants = new String[]{"888695842", "917104328", "989048880", "989049283", "997731670"};
        for (String identifier : participants) {
            scheduler.updateParticipant(identifier, endpoint);
        }
    }
}
