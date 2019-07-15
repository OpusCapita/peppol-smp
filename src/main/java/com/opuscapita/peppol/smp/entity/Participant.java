package com.opuscapita.peppol.smp.entity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name = "participants", indexes = {
        @Index(name = "ix_icd", columnList = "icd"),
        @Index(name = "ix_name", columnList = "name"),
        @Index(name = "ix_identifier", columnList = "identifier")
})
public class Participant {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icd", nullable = false, length = 10)
    private String icd;

    @Column(name = "identifier", nullable = false, length = 20)
    private String identifier;

    @Column(name = "country", length = 5)
    private String country;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "registered_at")
    private String registeredAt;

    @Version
    private Integer version;

    @ManyToMany
    @JoinTable(name = "participant_document_type",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "document_type_id"))
    private Set<DocumentType> documentTypes;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "endpoint_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Endpoint endpoint;

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
}
