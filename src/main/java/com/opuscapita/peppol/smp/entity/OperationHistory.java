package com.opuscapita.peppol.smp.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@DynamicUpdate
@Table(name = "operation_history")
public class OperationHistory {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "user", nullable = false)
    private String user;

    @Column(name = "participant", nullable = false)
    private String participant;

    @Column(name = "type", length = 10)
    @Enumerated(EnumType.STRING)
    private OperationType type;

    @Column(name = "date")
    private Date date;

    @Column(name = "description")
    private String description;

    @Version
    private Integer version;

    public OperationHistory() {
        this.date = new Date();
    }

    public OperationHistory(String user) {
        this();
        this.user = user;
    }

    public OperationHistory(String user, String participant) {
        this(user);
        this.participant = participant;
    }

    public OperationHistory(String user, String participant, OperationType type) {
        this(user, participant);
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
