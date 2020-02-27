package com.opuscapita.peppol.smp.entity;

import com.opuscapita.peppol.smp.controller.dto.DocumentTypeDto;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static Set<DocumentType> of(Set<DocumentTypeDto> documentTypeDtos) {
        if (documentTypeDtos == null || documentTypeDtos.isEmpty()) {
            return new HashSet<>();
        }
        return documentTypeDtos.stream().map(DocumentType::of).collect(Collectors.toSet());
    }

    public static DocumentType of(DocumentTypeDto documentTypeDto) {
        if (documentTypeDto == null) {
            return null;
        }

        DocumentType dto = new DocumentType();
        dto.setInternalId(documentTypeDto.getInternalId());
        return dto;
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
