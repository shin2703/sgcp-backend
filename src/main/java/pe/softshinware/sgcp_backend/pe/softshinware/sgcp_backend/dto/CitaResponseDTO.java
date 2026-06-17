package pe.softshinware.sgcp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponseDTO {
    private Long id;
    private String codigo;
    private LocalDateTime fechaHora;
    private String estado;
    private String pacienteNombre;
    private String psicologoNombre;
    private String especialidadNombre;
    private String deudaCodigo;
    private BigDecimal deudaMonto;
    private String deudaEstado;
}