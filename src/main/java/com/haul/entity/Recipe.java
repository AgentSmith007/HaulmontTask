package com.haul.entity;

import com.haul.Prioritet;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description = "";

    @ManyToOne(cascade =CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pacient_id")
    private Pacient pacient;

    @Column(name = "creationdate")
    private Date creationDate;

    @Column(name = "durationdate")
    private Date durationDate;

    @Column(name = "priority")
    private Prioritet prioritet;



    @Override
    public String toString() {
        return "Recipe{" +
                "description='" + description + '\'' +
                ", doctor=" + doctor +
                ", pacient=" + pacient +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Pacient getPacient() {
        return pacient;
    }

    public void setPacient(Pacient pacient) {
        this.pacient = pacient;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDurationDate() {
        return durationDate;
    }

    public void setDurationDate(Date durationDate) {
        this.durationDate = durationDate;
    }
    public Prioritet getPrioritet() {
        return prioritet;
    }

    public void setPrioritet(Prioritet prioritet) {
        this.prioritet = prioritet;
    }


}
