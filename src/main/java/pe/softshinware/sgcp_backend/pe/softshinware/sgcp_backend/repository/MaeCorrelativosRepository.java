package pe.softshinware.sgcp_backend.repository;

import pe.softshinware.sgcp_backend.entity.MaeCorrelativos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaeCorrelativosRepository extends JpaRepository<MaeCorrelativos, Long> {
    Optional<MaeCorrelativos> findByDescripcion(String descripcion);
}