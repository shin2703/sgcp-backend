package pe.softshinware.sgcp_backend.repository;

import pe.softshinware.sgcp_backend.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    Optional<Cita> findByCodigo(String codigo);

    List<Cita> findByPacienteId(Long pacienteId);

    boolean existsByPsicologoIdAndFechaHoraAndEstadoNot(
            Long psicologoId, LocalDateTime fechaHora, String estadoExcluido);

    boolean existsByPsicologoIdAndFechaHoraBetweenAndEstadoNot(
            Long psicologoId, LocalDateTime inicio, LocalDateTime fin, String estadoExcluido);
}