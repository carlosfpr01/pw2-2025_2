package dev.ifrs.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class User extends PanacheEntity {

    private String name;
    private String email;

    @Column(nullable = false, length = 255)
    public String password;

}
