package pe.softshinware.sgcp_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "historia_clinica")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", unique = true, nullable = false, length = 20)
    private String numero;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(length = 20)
    private String estado = "activo";

    @OneToOne
    @JoinColumn(name = "paciente_id", unique = true)
    private Paciente paciente;
}