package com.example.demo.repository.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "islands")
public class Island {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "island", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<Workstation> workstations = new ArrayList<>();

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Workstation> getWorkstations() {
        return workstations;
    }
    public void setWorkstations(List<Workstation> workstations) {
        this.workstations = workstations;
    }

    public Workstation alocar(User user) { 
        for (Workstation ws : this.workstations) {
            if (ws.isAvailable()) {
                ws.ocupar(user);
                return ws;
            }
        }

        throw new com.example.demo.domain.NoAvailableWorkstationException(
            "Nenhuma workstation disponível na ilha " + this.name
        );
    }

    public void addWorkstation(Workstation workstation) {
        this.workstations.add(workstation);
        workstation.setIsland(this);
    }
}