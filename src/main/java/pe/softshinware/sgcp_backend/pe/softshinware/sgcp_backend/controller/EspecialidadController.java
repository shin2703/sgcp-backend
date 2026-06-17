package pe.softshinware.sgcp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.softshinware.sgcp_backend.entity.Especialidad;
import pe.softshinware.sgcp_backend.repository.EspecialidadRepository;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadRepository especialidadRepository;

    @GetMapping
    public List<Especialidad> listarActivas() {
        return especialidadRepository.findByEstado("activo");
    }
}