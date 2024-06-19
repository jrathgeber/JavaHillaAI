package com.example.application.data;

import jakarta.persistence.Entity;

@Entity
public class CloTrade extends AbstractEntity {

    private String clo;
    private String dir;
    private long quantity;
    private double price;
    private boolean cancelled;

    public CloTrade(){};

    public CloTrade(String clo, String dir, long quantity, double price, boolean cancelled) {
        this.clo = clo;
        this.dir = dir;
        this.quantity = quantity;
        this.price = price;
        this.cancelled = cancelled;
    }

    public String getClo() {
        return clo;
    }

    public void setClo(String clo) {
        this.clo = clo;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
