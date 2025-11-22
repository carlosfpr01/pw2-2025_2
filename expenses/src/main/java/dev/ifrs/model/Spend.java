package main.java.dev.ifrs.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Spend extends PanacheEntity {
    @Column(nullable = false)
    public Long idUser;

    @Column(nullable = false)
    public Double amount;

    @Column(nullable = false)
    public String type;

    @Column(nullable = false)
    public String tag;

    @Column(nullable = false)
    public String date;

    public Spend() {}
    public Spend(Long idUser, Double amount, String type, String tag, String date) {
        this.idUser = idUser;
        this.amount = amount;
        this.type = type;
        this.tag = tag;
        this.date = date;
    }

    public Long getIdUser() {
        return idUser;
    }
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

}
