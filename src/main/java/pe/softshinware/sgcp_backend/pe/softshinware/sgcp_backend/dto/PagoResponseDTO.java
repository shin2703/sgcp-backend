package pe.softshinware.sgcp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDTO {
    private Long id;
    private String numero;
    private String tipo;
    private BigDecimal monto;
    private String medioPago;
    private String citaCodigo;
    private String citaEstado;
}