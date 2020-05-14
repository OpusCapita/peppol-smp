package com.opuscapita.peppol.smp.entity;

import com.opuscapita.peppol.smp.controller.dto.ParticipantDto;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name = "participants")
public class Participant {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icd", nullable = false, length = 20)
    private String icd;

    @Column(name = "identifier", nullable = false, length = 50)
    private String identifier;

    @Column(name = "business_platform", length = 20)
    @Enumerated(EnumType.STRING)
    private BusinessPlatform businessPlatform;

    @Column(name = "country", length = 5)
    private String country;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "registered_at")
    private String registeredAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "participant_document_type",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "document_type_id"))
    private Set<DocumentType> documentTypes;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "endpoint_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Endpoint endpoint;

    @Version
    private Integer version;

    public Participant() {
        this.documentTypes = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcd() {
        return icd;
    }

    public void setIcd(String icd) {
        this.icd = icd;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIcdIdentifier() {
        return this.icd + ":" + this.identifier;
    }

    public BusinessPlatform getBusinessPlatform() {
        return businessPlatform;
    }

    public void setBusinessPlatform(BusinessPlatform businessPlatform) {
        this.businessPlatform = businessPlatform;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Set<DocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(Set<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Participant copy(ParticipantDto participantDto) {
        setIcd(participantDto.getIcd());
        setName(participantDto.getName());
        setIdentifier(participantDto.getIdentifier());
        setBusinessPlatform(participantDto.getBusinessPlatform());
        setCountry(participantDto.getCountry());
        setContactName(participantDto.getContactName());
        setContactEmail(participantDto.getContactEmail());
        setContactPhone(participantDto.getContactPhone());

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return icd.equals(that.icd) && identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icd, identifier);
    }
}
