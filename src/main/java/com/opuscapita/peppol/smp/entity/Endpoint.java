package com.opuscapita.peppol.smp.entity;

import com.opuscapita.peppol.smp.repository.EndpointType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name = "endpoints")
public class Endpoint {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EndpointType type;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "smp_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Smp smp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EndpointType getType() {
        return type;
    }

    public void setType(EndpointType type) {
        this.type = type;
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
}
