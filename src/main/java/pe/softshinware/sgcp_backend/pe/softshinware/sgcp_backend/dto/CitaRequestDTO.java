package pe.softshinware.sgcp_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaRequestDTO {
    private Long pacienteId;
    private Long psicologoId;
    private Long especialidadId;
    private LocalDateTime fechaHora;
}