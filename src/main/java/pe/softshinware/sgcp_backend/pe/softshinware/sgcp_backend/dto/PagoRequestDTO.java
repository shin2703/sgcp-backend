package pe.softshinware.sgcp_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El ID de la deuda es obligatorio")
    private Long deudaId;

    @NotBlank(message = "El tipo de comprobante es obligatorio")
    private String tipoComprobante;

    @NotBlank(message = "El medio de pago es obligatorio")
    private String medioPago;
}