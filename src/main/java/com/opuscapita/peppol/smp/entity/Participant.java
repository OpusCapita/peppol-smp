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

    @Column(name = "country", length = 5)
    private String country;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "registered_at")
    private String registeredAt;

    @ManyToMany
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

    public static Participant of(ParticipantDto participantDto) {
        Participant participant = new Participant();

        participant.setId(participantDto.getId());
        participant.setIcd(participantDto.getIcd());
        participant.setName(participantDto.getName());
        participant.setIdentifier(participantDto.getIdentifier());
        participant.setCountry(participantDto.getCountry());
        participant.setContactInfo(participantDto.getContactInfo());
        participant.setRegisteredAt(participantDto.getRegisteredAt());

        return participant;
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
