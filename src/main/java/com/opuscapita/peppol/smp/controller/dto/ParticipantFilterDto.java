package com.opuscapita.peppol.smp.controller.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class ParticipantFilterDto {

    private String icd;
    private String name;
    private String identifier;
    private List<String> countries;

    public String getIcd() {
        return icd;
    }

    public void setIcd(String icd) {
        this.icd = icd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
