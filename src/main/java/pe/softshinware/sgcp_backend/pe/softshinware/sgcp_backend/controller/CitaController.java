package pe.softshinware.sgcp_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.softshinware.sgcp_backend.dto.CitaRequestDTO;
import pe.softshinware.sgcp_backend.dto.CitaResponseDTO;
import pe.softshinware.sgcp_backend.entity.Cita;
import pe.softshinware.sgcp_backend.entity.Deuda;
import pe.softshinware.sgcp_backend.repository.DeudaRepository;
import pe.softshinware.sgcp_backend.service.CitaService;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;
    private final DeudaRepository deudaRepository;

    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> listarCitas() {
        List<Cita> citas = citaService.listarCitas();
        List<CitaResponseDTO> response = citas.stream().map(cita -> {
            Deuda deuda = deudaRepository.findByCitaId(cita.getId()).orElse(null);
            return new CitaResponseDTO(
                    cita.getId(),
                    cita.getCodigo(),
                    cita.getFechaHora(),
                    cita.getEstado(),
                    cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                    cita.getPsicologo().getNombre() + " " + cita.getPsicologo().getApellido(),
                    cita.getEspecialidad().getNombre(),
                    deuda != null ? deuda.getCodigo() : "",
                    deuda != null ? deuda.getMonto() : null,
                    deuda != null ? deuda.getEstado() : ""
            );
        }).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> registrarCita(@Valid @RequestBody CitaRequestDTO request) {
        Cita cita = citaService.registrarCita(
                request.getPacienteId(),
                request.getPsicologoId(),
                request.getEspecialidadId(),
                request.getFechaHora()
        );

        Deuda deuda = deudaRepository.findByCitaId(cita.getId()).orElse(null);

        CitaResponseDTO response = new CitaResponseDTO(
                cita.getId(),
                cita.getCodigo(),
                cita.getFechaHora(),
                cita.getEstado(),
                cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
                cita.getPsicologo().getNombre() + " " + cita.getPsicologo().getApellido(),
                cita.getEspecialidad().getNombre(),
                deuda != null ? deuda.getCodigo() : "",
                deuda != null ? deuda.getMonto() : null,
                deuda != null ? deuda.getEstado() : ""
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}