package pe.softshinware.sgcp_backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.softshinware.sgcp_backend.entity.*;
import pe.softshinware.sgcp_backend.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock private CitaRepository citaRepository;
    @Mock private DeudaRepository deudaRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private PsicologoRepository psicologoRepository;
    @Mock private EspecialidadRepository especialidadRepository;
    @Mock private HistoriaClinicaRepository historiaClinicaRepository;
    @Mock private CorrelativoService correlativoService;

    @InjectMocks
    private CitaService citaService;

    private Paciente paciente;
    private Psicologo psicologo;
    private Especialidad especialidad;
    private LocalDateTime fechaHora;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Ana");
        paciente.setApellido("Torres");

        psicologo = new Psicologo();
        psicologo.setId(32L);
        psicologo.setNombre("Carlos");
        psicologo.setApellido("Mendoza");

        especialidad = new Especialidad();
        especialidad.setId(23L);
        especialidad.setNombre("Psicología Clínica");
        especialidad.setTarifa(new BigDecimal("150.00"));

        fechaHora = LocalDateTime.of(2026, 8, 20, 10, 0);
    }

    @Test
    void registrarCita_datosValidos_creaCitaYDeuda() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(psicologoRepository.findById(32L)).thenReturn(Optional.of(psicologo));
        when(especialidadRepository.findById(23L)).thenReturn(Optional.of(especialidad));
        when(historiaClinicaRepository.findByPacienteId(1L)).thenReturn(Optional.of(new HistoriaClinica()));
        when(citaRepository.existsByPsicologoIdAndFechaHoraBetweenAndEstadoNot(
                eq(32L), any(LocalDateTime.class), any(LocalDateTime.class), eq("cancelada")))
                .thenReturn(false);
        when(correlativoService.generarCodigo(eq("Cita"), eq("CIT"))).thenReturn("CIT000001");
        when(correlativoService.generarCodigo(eq("Deuda"), eq("DEU"))).thenReturn("DEU000001");
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));
        when(deudaRepository.save(any(Deuda.class))).thenAnswer(inv -> inv.getArgument(0));

        Cita resultado = citaService.registrarCita(1L, 32L, 23L, fechaHora);

        assertNotNull(resultado);
        assertEquals("CIT000001", resultado.getCodigo());
        assertEquals("registrada", resultado.getEstado());
        verify(deudaRepository, times(1)).save(any(Deuda.class));
    }

    @Test
    void registrarCita_psicologoOcupado_lanzaExcepcion() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(psicologoRepository.findById(32L)).thenReturn(Optional.of(psicologo));
        when(especialidadRepository.findById(23L)).thenReturn(Optional.of(especialidad));
        when(historiaClinicaRepository.findByPacienteId(1L)).thenReturn(Optional.of(new HistoriaClinica()));
        when(citaRepository.existsByPsicologoIdAndFechaHoraBetweenAndEstadoNot(
                eq(32L), any(LocalDateTime.class), any(LocalDateTime.class), eq("cancelada")))
                .thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> citaService.registrarCita(1L, 32L, 23L, fechaHora));

        assertTrue(ex.getMessage().contains("R0013"));
        verify(citaRepository, never()).save(any());
    }

    @Test
    void registrarCita_pacienteInexistente_lanzaExcepcion() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(jakarta.persistence.EntityNotFoundException.class,
                () -> citaService.registrarCita(99L, 32L, 23L, fechaHora));
    }

    @Test
    void registrarCita_deudaGeneradaConMontoDeTarifa() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(psicologoRepository.findById(32L)).thenReturn(Optional.of(psicologo));
        when(especialidadRepository.findById(23L)).thenReturn(Optional.of(especialidad));
        when(historiaClinicaRepository.findByPacienteId(1L)).thenReturn(Optional.of(new HistoriaClinica()));
        when(citaRepository.existsByPsicologoIdAndFechaHoraBetweenAndEstadoNot(
                eq(32L), any(LocalDateTime.class), any(LocalDateTime.class), eq("cancelada")))
                .thenReturn(false);
        when(correlativoService.generarCodigo(eq("Cita"), eq("CIT"))).thenReturn("CIT000002");
        when(correlativoService.generarCodigo(eq("Deuda"), eq("DEU"))).thenReturn("DEU000002");
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        Deuda[] deudaCapturada = new Deuda[1];
        when(deudaRepository.save(any(Deuda.class))).thenAnswer(inv -> {
            deudaCapturada[0] = inv.getArgument(0);
            return deudaCapturada[0];
        });

        citaService.registrarCita(1L, 32L, 23L, fechaHora);

        assertEquals(new BigDecimal("150.00"), deudaCapturada[0].getMonto());
        assertEquals("gastos de cita", deudaCapturada[0].getConcepto());
        assertEquals("pendiente", deudaCapturada[0].getEstado());
    }
}