package pe.softshinware.sgcp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.softshinware.sgcp_backend.entity.*;
import pe.softshinware.sgcp_backend.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final DeudaRepository deudaRepository;
    private final PacienteRepository pacienteRepository;
    private final PsicologoRepository psicologoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final CorrelativoService correlativoService;

    @Transactional
    public Cita registrarCita(Long pacienteId, Long psicologoId, Long especialidadId, LocalDateTime fechaHora) {

        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado: " + pacienteId));

        Psicologo psicologo = psicologoRepository.findById(psicologoId)
                .orElseThrow(() -> new EntityNotFoundException("Psicólogo no encontrado: " + psicologoId));

        Especialidad especialidad = especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new EntityNotFoundException("Especialidad no encontrada: " + especialidadId));

        // Flujo alterno: si no tiene HC activa, se apertura automáticamente
        historiaClinicaRepository.findByPacienteId(pacienteId)
                .orElseGet(() -> {
                    HistoriaClinica nuevaHc = new HistoriaClinica();
                    nuevaHc.setNumero(correlativoService.generarCodigo("HistoriaClinica", "HC"));
                    nuevaHc.setFechaApertura(LocalDate.now());
                    nuevaHc.setEstado("activo");
                    nuevaHc.setPaciente(paciente);
                    return historiaClinicaRepository.save(nuevaHc);
                });

        // R0013: impedir citas en el mismo bloque de 1 hora para el mismo psicólogo
        LocalDateTime bloqueInicio = fechaHora.minusMinutes(59);
        LocalDateTime bloqueFin = fechaHora.plusMinutes(59);

        boolean traslape = citaRepository.existsByPsicologoIdAndFechaHoraBetweenAndEstadoNot(
                psicologoId, bloqueInicio, bloqueFin, "cancelada");
        if (traslape) {
            throw new IllegalStateException(
                    "El psicólogo ya tiene una cita en ese bloque horario (R0013). Debe haber al menos 1 hora de diferencia entre citas.");
        }

        Cita cita = new Cita();
        cita.setCodigo(correlativoService.generarCodigo("Cita", "CIT"));
        cita.setFechaHora(fechaHora);
        cita.setEstado("registrada");
        cita.setPaciente(paciente);
        cita.setPsicologo(psicologo);
        cita.setEspecialidad(especialidad);
        citaRepository.save(cita);

        // R0012: generar deuda automática con monto de la tarifa
        Deuda deuda = new Deuda();
        deuda.setCodigo(correlativoService.generarCodigo("Deuda", "DEU"));
        deuda.setConcepto("gastos de cita");
        deuda.setMonto(especialidad.getTarifa());
        deuda.setEstado("pendiente");
        deuda.setCita(cita);
        deudaRepository.save(deuda);

        return cita;
    }
}