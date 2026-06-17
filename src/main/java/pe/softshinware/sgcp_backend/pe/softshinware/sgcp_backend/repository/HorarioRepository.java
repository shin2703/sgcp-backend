package pe.softshinware.sgcp_backend.repository;

import pe.softshinware.sgcp_backend.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findByPsicologoId(Long psicologoId);
}