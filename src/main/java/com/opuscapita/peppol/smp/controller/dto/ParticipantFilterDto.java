package com.opuscapita.peppol.smp.controller.dto;

import com.opuscapita.peppol.smp.entity.BusinessPlatform;
import com.opuscapita.peppol.smp.repository.EndpointType;
import com.opuscapita.peppol.smp.repository.SmpName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class ParticipantFilterDto {

    private String icd;
    private String name;
    private String identifier;
    private List<String> countries;
    private List<SmpName> smpNames;
    private List<EndpointType> endpointTypes;
    private List<BusinessPlatform> businessPlatforms;

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

    public List<SmpName> getSmpNames() {
        return smpNames;
    }

    public void setSmpNames(List<SmpName> smpNames) {
        this.smpNames = smpNames;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<EndpointType> getEndpointTypes() {
        return endpointTypes;
    }

    public void setEndpointTypes(List<EndpointType> endpointTypes) {
        this.endpointTypes = endpointTypes;
    }

    public List<BusinessPlatform> getBusinessPlatforms() {
        return businessPlatforms;
    }

    public void setBusinessPlatforms(List<BusinessPlatform> businessPlatforms) {
        this.businessPlatforms = businessPlatforms;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
