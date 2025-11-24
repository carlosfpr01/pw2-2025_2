package run.gastos.model;

import java.time.LocalDate;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Despesa extends PanacheEntity {

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false)
    private Number amount;

    public enum operations{
        D,
        C
    }
    @Column(nullable = false)
    private operations operation;

    @Column(nullable = true)
    private String tag;

    @Column(nullable = false)
    private LocalDate date;

    public Despesa() {}
    public Despesa(Long idUser, Double amount, operations operation, String tag, LocalDate date) {
        this.idUser = idUser;
        this.amount = amount;
        this.operation = operation;
        this.tag = tag;
        this.date = date;
    }

    public Long getIdUser() {
        return idUser;
    }
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Number getAmount() {
        return amount;
    }
    public void setAmount(Number amount) {
        this.amount = amount;
    }

    public String getOperation() {
        return operation.name();
    }
    public void setOperation(operations operation) {
        this.operation = operation;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

}
