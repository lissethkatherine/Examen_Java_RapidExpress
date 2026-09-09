package com.rapidexpress.view;

import com.rapidexpress.controller.MultaConductorController;
import com.rapidexpress.model.AbonoInvalidoException;
import com.rapidexpress.model.MultaConductor;
import com.rapidexpress.service.MetodoPago;
import com.rapidexpress.service.PagoEfectivo;
import com.rapidexpress.service.PagoTransferencia;
import com.rapidexpress.util.ConsoleUtil;

import java.math.BigDecimal;
import java.util.List;

/** Funcionalidad nueva: Gestion de Multas y Pagos de Conductores. */
public class MultaConductorView {

    private final MultaConductorController controller = new MultaConductorController();
    private final String usuarioOperador;

    public MultaConductorView(String usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            ConsoleUtil.imprimirEncabezado("GESTION DE MULTAS Y PAGOS DE CONDUCTORES");
            System.out.println("1. Ver conductores con saldo pendiente y registrar abono");
            System.out.println("2. Ver historial de multas de un conductor");
            System.out.println("0. Volver al menu principal");
            opcion = ConsoleUtil.leerOpcion("Seleccione una opcion: ", 0, 2);

            switch (opcion) {
                case 1 -> verSaldosYRegistrarAbono();
                case 2 -> verHistorial();
                case 0 -> System.out.println("Volviendo al menu principal...");
            }
        } while (opcion != 0);
    }

    private void verSaldosYRegistrarAbono() {
        ConsoleUtil.imprimirEncabezado("CONDUCTORES CON SALDO PENDIENTE");
        List<MultaConductor> pendientes = controller.listarConSaldoPendiente();

        if (pendientes.isEmpty()) {
            System.out.println("No hay conductores con saldo pendiente. Todo al dia.");
            ConsoleUtil.pausar();
            return;
        }

        for (MultaConductor m : pendientes) {
            String nombre = controller.nombreConductor(m.getIdConductor());
            System.out.printf("%d. %s (Multa #%d - %s) -> $%,.0f%n",
                    m.getIdConductor(), nombre, m.getIdMulta(), m.getMotivo(), m.getSaldoPendiente());
        }
        System.out.printf("%nTotal adeudado: $%,.0f%n%n", controller.totalAdeudado());

        int idMulta = ConsoleUtil.leerEntero("Ingrese el ID de la MULTA para registrar abono (0 para cancelar): ");
        if (idMulta == 0) {
            return;
        }

        BigDecimal monto = ConsoleUtil.leerDecimal("Monto del abono: ");

        System.out.println("Metodo de pago: 1. Efectivo  2. Transferencia");
        int op = ConsoleUtil.leerOpcion("Seleccione: ", 1, 2);
        MetodoPago metodo;
        if (op == 1) {
            metodo = new PagoEfectivo();
        } else {
            String ref = ConsoleUtil.leerTexto("Numero de referencia de la transferencia: ");
            metodo = new PagoTransferencia(ref);
        }

        try {
            BigDecimal nuevoSaldo = controller.registrarAbono(idMulta, monto, metodo, usuarioOperador);
            System.out.println("\nAbono registrado exitosamente.");
            System.out.printf("Nuevo saldo: $%,.0f%n", nuevoSaldo);
            System.out.println("Transaccion guardada en abonos_multas.txt");
        } catch (AbonoInvalidoException e) {
            System.out.println("\n>> Error: " + e.getMessage());
        }

        ConsoleUtil.pausar();
    }

    private void verHistorial() {
        ConsoleUtil.imprimirEncabezado("Historial de multas por conductor");
        int idConductor = ConsoleUtil.leerEntero("Id del conductor: ");
        List<MultaConductor> historial = controller.historialPorConductor(idConductor);
        if (historial.isEmpty()) {
            System.out.println("Este conductor no tiene multas registradas.");
        } else {
            historial.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }
}
