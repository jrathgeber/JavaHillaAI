package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class CloReport extends AbstractEntity {

    @NotEmpty
    private String clo;
    @NotEmpty
    private String name;
    @NotEmpty
    private String data;

    public CloReport() {
    }

    public CloReport(String clo, String name, String data) {
        this.clo = clo;
        this.name = name;
        this.data = data;
    }

    public String getClo() {
        return clo;
    }

    public void setClo(String clo) {
        this.clo = clo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
