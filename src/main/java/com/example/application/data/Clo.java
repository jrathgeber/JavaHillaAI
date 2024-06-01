package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Clo extends AbstractEntity {

    @NotEmpty
    private String name;

    @NotEmpty
    private String location;

    public Clo() {
    }

    public Clo(String firstName, String location) {
        this.name = firstName;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
