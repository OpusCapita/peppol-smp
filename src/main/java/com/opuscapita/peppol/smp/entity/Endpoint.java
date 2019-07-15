package com.opuscapita.peppol.smp.entity;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name = "endpoints")
public class Endpoint {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "endpoint_id")
    private int endpointId;

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

    public int getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(int endpointId) {
        this.endpointId = endpointId;
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
