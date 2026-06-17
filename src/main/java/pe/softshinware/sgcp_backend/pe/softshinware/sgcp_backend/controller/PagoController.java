package pe.softshinware.sgcp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.softshinware.sgcp_backend.dto.PagoRequestDTO;
import pe.softshinware.sgcp_backend.dto.PagoResponseDTO;
import pe.softshinware.sgcp_backend.entity.ComprobantePago;
import pe.softshinware.sgcp_backend.entity.Deuda;
import pe.softshinware.sgcp_backend.service.PagoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping("/deudas/paciente/{pacienteId}")
    public ResponseEntity<List<Deuda>> buscarDeudasPendientes(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(pagoService.buscarDeudasPendientes(pacienteId));
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrarPago(@RequestBody PagoRequestDTO request) {
        ComprobantePago comprobante = pagoService.registrarPago(
                request.getDeudaId(),
                request.getTipoComprobante(),
                request.getMedioPago()
        );

        PagoResponseDTO response = new PagoResponseDTO(
                comprobante.getId(),
                comprobante.getNumero(),
                comprobante.getTipo(),
                comprobante.getMonto(),
                comprobante.getMedioPago(),
                comprobante.getDeuda().getCita().getCodigo(),
                comprobante.getDeuda().getCita().getEstado()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}