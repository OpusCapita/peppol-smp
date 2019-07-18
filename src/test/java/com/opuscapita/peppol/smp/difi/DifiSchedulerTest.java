package com.opuscapita.peppol.smp.difi;

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

    @Test
    @Ignore
    public void testScheduler() {
        scheduler.updateLocalDatabase();
    }
}
