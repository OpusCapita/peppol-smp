package com.opuscapita.peppol.smp.entity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name = "document_types")
public class DocumentType {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "document_type_id")
    private Integer documentTypeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profile_identifier")
    private String profileIdentifier;

    @Column(name = "document_identifier")
    private String documentIdentifier;

    @ManyToMany(mappedBy = "documentTypes")
    private Set<Participant> participants;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "smp_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Smp smp;

    public DocumentType() {
        this.participants = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Integer documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileIdentifier() {
        return profileIdentifier;
    }

    public void setProfileIdentifier(String profileIdentifier) {
        this.profileIdentifier = profileIdentifier;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    public Smp getSmp() {
        return smp;
    }

    public void setSmp(Smp smp) {
        this.smp = smp;
    }
}
