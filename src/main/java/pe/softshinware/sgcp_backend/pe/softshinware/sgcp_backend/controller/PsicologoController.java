package pe.softshinware.sgcp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.softshinware.sgcp_backend.entity.Psicologo;
import pe.softshinware.sgcp_backend.repository.PsicologoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/psicologos")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PsicologoController {

    private final PsicologoRepository psicologoRepository;

    @GetMapping("/especialidad/{especialidadId}")
    public List<Psicologo> listarPorEspecialidad(@PathVariable Long especialidadId) {
        return psicologoRepository.findByEspecialidadIdAndEstado(especialidadId, "activo");
    }
}