package pe.softshinware.sgcp_backend.repository;

import pe.softshinware.sgcp_backend.entity.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeudaRepository extends JpaRepository<Deuda, Long> {

    Optional<Deuda> findByCitaId(Long citaId);

    List<Deuda> findByCita_Paciente_IdAndEstado(Long pacienteId, String estado);
}