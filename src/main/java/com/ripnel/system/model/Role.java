package com.ripnel.system.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles") // nombre real de la tabla
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String name; // ADMIN, ALMACEN, COMPRAS, PRODUCCION, VENDEDOR

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Recomendado para que Set<Role> funcione bien
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role other)) return false;
        return id != null && id.equals(other.id);
    }
    @Override public int hashCode() { return getClass().hashCode(); }
}
