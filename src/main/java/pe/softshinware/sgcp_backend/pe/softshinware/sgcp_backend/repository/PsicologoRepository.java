package pe.softshinware.sgcp_backend.repository;

import pe.softshinware.sgcp_backend.entity.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsicologoRepository extends JpaRepository<Psicologo, Long> {
    List<Psicologo> findByEspecialidadIdAndEstado(Long especialidadId, String estado);
}