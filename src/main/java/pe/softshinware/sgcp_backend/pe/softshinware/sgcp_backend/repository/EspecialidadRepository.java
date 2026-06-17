package pe.softshinware.sgcp_backend.repository;

import pe.softshinware.sgcp_backend.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    List<Especialidad> findByEstado(String estado);
}