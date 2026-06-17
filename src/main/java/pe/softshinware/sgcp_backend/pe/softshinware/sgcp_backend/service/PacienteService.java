package pe.softshinware.sgcp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.softshinware.sgcp_backend.entity.Paciente;
import pe.softshinware.sgcp_backend.repository.PacienteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final CorrelativoService correlativoService;

    public Paciente buscarPorDni(String dni) {
        return pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No existe paciente con DNI: " + dni));
    }

    public Paciente registrar(Paciente paciente) {
        if (paciente.getDni() == null || paciente.getDni().length() != 8) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos (R0008)");
        }
        paciente.setCodigo(correlativoService.generarCodigo("Paciente", "PAC"));
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }
}