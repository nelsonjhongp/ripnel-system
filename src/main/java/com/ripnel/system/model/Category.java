package com.ripnel.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity @Table(name="categories", uniqueConstraints=@UniqueConstraint(columnNames="name"))
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable=false, unique=true)
    private String name;

    @Column(nullable=false)
    private Boolean active = true;

    // getters/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
