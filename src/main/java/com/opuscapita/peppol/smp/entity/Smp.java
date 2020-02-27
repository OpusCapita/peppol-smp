package com.opuscapita.peppol.smp.entity;

import com.opuscapita.peppol.smp.repository.SmpName;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name = "smps")
public class Smp {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private SmpName name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SmpName getName() {
        return name;
    }

    public void setName(SmpName name) {
        this.name = name;
    }
}
