package com.rapidexpress.controller;

import com.rapidexpress.dao.ConductorDAO;
import com.rapidexpress.dao.MultaConductorDAO;
import com.rapidexpress.dao.impl.ConductorDAOImpl;
import com.rapidexpress.dao.impl.MultaConductorDAOImpl;
import com.rapidexpress.model.AbonoInvalidoException;
import com.rapidexpress.model.Conductor;
import com.rapidexpress.model.MultaConductor;
import com.rapidexpress.service.AuditoriaService;
import com.rapidexpress.service.MetodoPago;
import com.rapidexpress.util.AbonoFileLogger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Funcionalidad nueva: "Gestion de Multas y Pagos de Conductores".
 * Aplica SRP (Single Responsibility): esta clase solo orquesta las reglas
 * de negocio de multas/abonos, no hace SQL directo (eso vive en el DAO) ni
 * imprime en consola (eso vive en la Vista).
 */
public class MultaConductorController {

    private final MultaConductorDAO multaDAO;
    private final ConductorDAO conductorDAO;
    private final AuditoriaService auditoriaService;

    public MultaConductorController() {
        this(new MultaConductorDAOImpl(), new ConductorDAOImpl(), new AuditoriaService());
    }

    public MultaConductorController(MultaConductorDAO multaDAO, ConductorDAO conductorDAO,
                                     AuditoriaService auditoriaService) {
        this.multaDAO = multaDAO;
        this.conductorDAO = conductorDAO;
        this.auditoriaService = auditoriaService;
    }

    /**
     * Lista los conductores con saldo pendiente, ordenados de mayor a
     * menor deuda (Streams + Collections, tal como pide el enunciado).
     */
    public List<MultaConductor> listarConSaldoPendiente() {
        return multaDAO.listarConSaldoPendiente().stream()
                .filter(m -> m.getSaldoPendiente().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(MultaConductor::getSaldoPendiente).reversed())
                .collect(Collectors.toList());
    }

    /** Monto total adeudado por todos los conductores (Stream + reduce). */
    public BigDecimal totalAdeudado() {
        return listarConSaldoPendiente().stream()
                .map(MultaConductor::getSaldoPendiente)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MultaConductor> historialPorConductor(int idConductor) {
        return multaDAO.listarPorConductor(idConductor);
    }

    /**
     * Registra un abono sobre una multa. Valida que el monto sea positivo
     * y que no exceda el saldo pendiente (excepcion personalizada). Usa el
     * patron Strategy (MetodoPago) para describir el pago sin acoplarse a
     * un tipo especifico (efectivo/transferencia).
     *
     * @return el nuevo saldo pendiente, ya actualizado en base de datos.
     */
    public BigDecimal registrarAbono(int idMulta, BigDecimal montoAbono, MetodoPago metodoPago,
                                      String usuarioOperador) throws AbonoInvalidoException {

        Optional<MultaConductor> multaOpt = multaDAO.buscarPorId(idMulta);
        if (multaOpt.isEmpty()) {
            throw new AbonoInvalidoException("La multa #" + idMulta + " no existe.");
        }
        MultaConductor multa = multaOpt.get();

        if (montoAbono.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AbonoInvalidoException("El monto del abono debe ser mayor a cero.");
        }
        if (montoAbono.compareTo(multa.getSaldoPendiente()) > 0) {
            throw new AbonoInvalidoException(String.format(
                    "El abono ($%,.0f) no puede ser mayor al saldo pendiente ($%,.0f).",
                    montoAbono, multa.getSaldoPendiente()));
        }

        BigDecimal nuevoSaldo = multa.getSaldoPendiente().subtract(montoAbono);

        // 1) Actualizar en base de datos (JDBC)
        boolean actualizado = multaDAO.actualizarSaldo(idMulta, nuevoSaldo);
        if (!actualizado) {
            throw new AbonoInvalidoException("No se pudo actualizar el saldo en la base de datos.");
        }

        // 2) Registrar en archivo de texto (persistencia), usando el Strategy elegido
        String descripcion = metodoPago.describir(montoAbono);
        try {
            AbonoFileLogger.registrar(idMulta, multa.getIdConductor(), descripcion, nuevoSaldo);
        } catch (IOException e) {
            throw new AbonoInvalidoException("El abono se aplico en la base de datos, pero no se pudo " +
                    "escribir en el archivo de registro: " + e.getMessage());
        }

        // 3) Auditoria del sistema (reutiliza el servicio ya existente)
        auditoriaService.registrar(usuarioOperador, "REGISTRAR_ABONO", "multas_conductor", idMulta,
                descripcion + " - Nuevo saldo: " + nuevoSaldo);

        return nuevoSaldo;
    }

    /** Nombre del conductor, usado por la vista para mostrar mensajes legibles. */
    public String nombreConductor(int idConductor) {
        return conductorDAO.buscarPorId(idConductor)
                .map(Conductor::getNombreCompleto)
                .orElse("Conductor #" + idConductor);
    }
}
