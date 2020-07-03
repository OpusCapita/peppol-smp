package com.opuscapita.peppol.smp.controller;

import com.opuscapita.peppol.smp.entity.BusinessPlatform;
import com.opuscapita.peppol.smp.repository.ParticipantService;
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
public class CommonTest {

    @Autowired
    private ParticipantService participantService;

    @Test
    @Ignore
    public void executeCommonTest() {
        BusinessPlatform businessPlatform = participantService.getBusinessPlatform("0007", "2021002916");
        Assert.assertEquals(businessPlatform, BusinessPlatform.XIB);
    }
}
