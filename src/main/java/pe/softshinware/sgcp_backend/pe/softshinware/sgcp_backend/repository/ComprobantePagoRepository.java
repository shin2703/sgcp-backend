package pe.softshinware.sgcp_backend.repository;

import pe.softshinware.sgcp_backend.entity.ComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {
    Optional<ComprobantePago> findByDeudaId(Long deudaId);
}