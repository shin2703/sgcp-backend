package pe.softshinware.sgcp_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "mae_correlativos")
public class MaeCorrelativos {

    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String descripcion;

    @Column(nullable = false)
    private Integer valor;
}