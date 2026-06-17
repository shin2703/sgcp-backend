package pe.softshinware.sgcp_backend.dto;

import lombok.Data;

@Data
public class PagoRequestDTO {
    private Long deudaId;
    private String tipoComprobante;
    private String medioPago;
}