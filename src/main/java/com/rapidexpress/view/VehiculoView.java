package com.rapidexpress.view;

import com.rapidexpress.controller.VehiculoController;
import com.rapidexpress.model.*;
import com.rapidexpress.util.ConsoleUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class VehiculoView {

    private final VehiculoController controller = new VehiculoController();
    private final String usuarioOperador;

    public VehiculoView(String usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            ConsoleUtil.imprimirEncabezado("GESTION DE FLOTA DE VEHICULOS");
            System.out.println("1. Registrar nuevo vehiculo");
            System.out.println("2. Listar todos los vehiculos");
            System.out.println("3. Listar vehiculos por estado");
            System.out.println("4. Actualizar estado de un vehiculo");
            System.out.println("5. Registrar mantenimiento");
            System.out.println("6. Ver historial de mantenimiento de un vehiculo");
            System.out.println("0. Volver al menu principal");
            opcion = ConsoleUtil.leerOpcion("Seleccione una opcion: ", 0, 6);

            switch (opcion) {
                case 1 -> registrarVehiculo();
                case 2 -> listarTodos();
                case 3 -> listarPorEstado();
                case 4 -> actualizarEstado();
                case 5 -> registrarMantenimiento();
                case 6 -> verHistorial();
                case 0 -> System.out.println("Volviendo al menu principal...");
            }
        } while (opcion != 0);
    }

    private void registrarVehiculo() {
        ConsoleUtil.imprimirEncabezado("Registrar nuevo vehiculo");
        String placa = ConsoleUtil.leerTexto("Placa: ");
        String marca = ConsoleUtil.leerTexto("Marca: ");
        String modelo = ConsoleUtil.leerTexto("Modelo: ");
        int anio = ConsoleUtil.leerEntero("Anio de fabricacion: ");
        BigDecimal capacidad = ConsoleUtil.leerDecimal("Capacidad de carga (kg): ");

        Vehiculo vehiculo = new Vehiculo(placa, marca, modelo, anio, capacidad);
        Vehiculo creado = controller.registrarVehiculo(vehiculo, usuarioOperador);
        System.out.println(">> Vehiculo registrado con id #" + creado.getIdVehiculo());
        ConsoleUtil.pausar();
    }

    private void listarTodos() {
        ConsoleUtil.imprimirEncabezado("Listado de vehiculos");
        List<Vehiculo> vehiculos = controller.listarTodos();
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehiculos registrados.");
        } else {
            vehiculos.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void listarPorEstado() {
        ConsoleUtil.imprimirEncabezado("Listar vehiculos por estado");
        System.out.println("1. Disponible  2. En Ruta  3. En Mantenimiento");
        int op = ConsoleUtil.leerOpcion("Seleccione: ", 1, 3);
        EstadoVehiculo estado = switch (op) {
            case 1 -> EstadoVehiculo.DISPONIBLE;
            case 2 -> EstadoVehiculo.EN_RUTA;
            default -> EstadoVehiculo.EN_MANTENIMIENTO;
        };
        List<Vehiculo> vehiculos = controller.listarPorEstado(estado);
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehiculos en estado " + estado + ".");
        } else {
            vehiculos.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void actualizarEstado() {
        ConsoleUtil.imprimirEncabezado("Actualizar estado de vehiculo");
        int id = ConsoleUtil.leerEntero("Id del vehiculo: ");
        System.out.println("1. Disponible  2. En Ruta  3. En Mantenimiento");
        int op = ConsoleUtil.leerOpcion("Nuevo estado: ", 1, 3);
        EstadoVehiculo estado = switch (op) {
            case 1 -> EstadoVehiculo.DISPONIBLE;
            case 2 -> EstadoVehiculo.EN_RUTA;
            default -> EstadoVehiculo.EN_MANTENIMIENTO;
        };
        boolean ok = controller.actualizarEstado(id, estado, usuarioOperador);
        System.out.println(ok ? ">> Estado actualizado." : ">> No se encontro el vehiculo indicado.");
        ConsoleUtil.pausar();
    }

    private void registrarMantenimiento() {
        ConsoleUtil.imprimirEncabezado("Registrar mantenimiento");
        int idVehiculo = ConsoleUtil.leerEntero("Id del vehiculo: ");
        System.out.println("1. Preventivo  2. Correctivo");
        int tipoOp = ConsoleUtil.leerOpcion("Tipo: ", 1, 2);
        String tipo = tipoOp == 1 ? "Preventivo" : "Correctivo";
        LocalDate fecha = ConsoleUtil.leerFecha("Fecha del mantenimiento");
        String descripcion = ConsoleUtil.leerTexto("Descripcion: ");
        BigDecimal costo = ConsoleUtil.leerDecimal("Costo: ");
        String taller = ConsoleUtil.leerTexto("Taller: ");

        MantenimientoVehiculo mantenimiento = new MantenimientoVehiculo(idVehiculo, tipo, fecha,
                descripcion, costo, taller);
        MantenimientoVehiculo creado = controller.registrarMantenimiento(mantenimiento, usuarioOperador);
        System.out.println(">> Mantenimiento registrado con id #" + creado.getIdMantenimiento()
                + ". El vehiculo #" + idVehiculo + " cambio a estado En Mantenimiento.");
        ConsoleUtil.pausar();
    }

    private void verHistorial() {
        ConsoleUtil.imprimirEncabezado("Historial de mantenimiento");
        int idVehiculo = ConsoleUtil.leerEntero("Id del vehiculo: ");
        List<MantenimientoVehiculo> historial = controller.historialMantenimiento(idVehiculo);
        if (historial.isEmpty()) {
            System.out.println("No hay mantenimientos registrados para este vehiculo.");
        } else {
            historial.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }
}
