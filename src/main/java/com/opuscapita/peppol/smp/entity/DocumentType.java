package com.opuscapita.peppol.smp.entity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@DynamicUpdate
@Table(name = "document_types")
public class DocumentType {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(name = "internal_id")
    private Integer internalId;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "smp_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Smp smp;

    public DocumentType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public Integer getExternalIdAsInteger() {
        return Integer.parseInt(externalId);
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setExternalIdAsInteger(Integer externalId) {
        this.externalId = String.valueOf(externalId);
    }

    public Integer getInternalId() {
        return internalId;
    }

    public void setInternalId(Integer internalId) {
        this.internalId = internalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Smp getSmp() {
        return smp;
    }

    public void setSmp(Smp smp) {
        this.smp = smp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentType that = (DocumentType) o;
        return Objects.equals(externalId, that.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId);
    }
}
