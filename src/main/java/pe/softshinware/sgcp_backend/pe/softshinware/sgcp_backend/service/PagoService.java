package pe.softshinware.sgcp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.softshinware.sgcp_backend.entity.Cita;
import pe.softshinware.sgcp_backend.entity.ComprobantePago;
import pe.softshinware.sgcp_backend.entity.Deuda;
import pe.softshinware.sgcp_backend.repository.ComprobantePagoRepository;
import pe.softshinware.sgcp_backend.repository.DeudaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final DeudaRepository deudaRepository;
    private final ComprobantePagoRepository comprobantePagoRepository;
    private final CorrelativoService correlativoService;

    // R0016: buscar deuda pendiente de un paciente
    public List<Deuda> buscarDeudasPendientes(Long pacienteId) {
        return deudaRepository.findByCita_Paciente_IdAndEstado(pacienteId, "pendiente");
    }

    @Transactional
    public ComprobantePago registrarPago(Long deudaId, String tipoComprobante, String medioPago) {

        Deuda deuda = deudaRepository.findById(deudaId)
                .orElseThrow(() -> new EntityNotFoundException("Deuda no encontrada: " + deudaId));

        if (!"pendiente".equals(deuda.getEstado())) {
            throw new IllegalStateException("La deuda ya fue cancelada o no está pendiente");
        }

        // R0019: deuda -> cancelada
        deuda.setEstado("cancelada");
        deudaRepository.save(deuda);

        // R0019: cita -> pagada
        Cita cita = deuda.getCita();
        cita.setEstado("pagada");
        // cita se actualiza en cascada al guardar la deuda gracias a la relación,
        // pero la guardamos explícitamente para que quede claro y seguro
        deudaRepository.save(deuda);

        // R0020/R0021/R0022: generar comprobante con monto EXACTO de la deuda cancelada
        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setNumero(correlativoService.generarCodigo("Comprobante", "COMP"));
        comprobante.setTipo(tipoComprobante);
        comprobante.setMonto(deuda.getMonto());
        comprobante.setMedioPago(medioPago);
        comprobante.setDeuda(deuda);

        return comprobantePagoRepository.save(comprobante);
    }
}