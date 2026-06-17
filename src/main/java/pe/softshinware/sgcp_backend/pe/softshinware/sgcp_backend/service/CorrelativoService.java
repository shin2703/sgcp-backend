package pe.softshinware.sgcp_backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.softshinware.sgcp_backend.entity.MaeCorrelativos;
import pe.softshinware.sgcp_backend.repository.MaeCorrelativosRepository;

@Service
@RequiredArgsConstructor
public class CorrelativoService {

    private final MaeCorrelativosRepository maeCorrelativosRepository;

    @Transactional
    public String generarCodigo(String descripcion, String prefijo) {
        MaeCorrelativos correlativo = maeCorrelativosRepository.findByDescripcion(descripcion)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Correlativo no configurado para: " + descripcion));

        int siguiente = correlativo.getValor();
        correlativo.setValor(siguiente + 1);
        maeCorrelativosRepository.save(correlativo);

        return prefijo + String.format("%06d", siguiente);
    }
}