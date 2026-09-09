package com.rapidexpress.view;

import com.rapidexpress.controller.ConductorController;
import com.rapidexpress.model.Conductor;
import com.rapidexpress.model.EstadoConductor;
import com.rapidexpress.util.ConsoleUtil;

import java.util.List;

public class ConductorView {

    private final ConductorController controller = new ConductorController();
    private final String usuarioOperador;

    public ConductorView(String usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            ConsoleUtil.imprimirEncabezado("GESTION DE PERSONAL (CONDUCTORES)");
            System.out.println("1. Registrar nuevo conductor");
            System.out.println("2. Listar todos los conductores");
            System.out.println("3. Listar conductores por estado");
            System.out.println("4. Actualizar estado de un conductor");
            System.out.println("5. Asignar conductor a vehiculo disponible");
            System.out.println("6. Ver historial de asignaciones");
            System.out.println("0. Volver al menu principal");
            opcion = ConsoleUtil.leerOpcion("Seleccione una opcion: ", 0, 6);

            switch (opcion) {
                case 1 -> registrarConductor();
                case 2 -> listarTodos();
                case 3 -> listarPorEstado();
                case 4 -> actualizarEstado();
                case 5 -> asignarAVehiculo();
                case 6 -> verAsignaciones();
                case 0 -> System.out.println("Volviendo al menu principal...");
            }
        } while (opcion != 0);
    }

    private void registrarConductor() {
        ConsoleUtil.imprimirEncabezado("Registrar nuevo conductor");
        String identificacion = ConsoleUtil.leerTexto("Numero de identificacion: ");
        String nombre = ConsoleUtil.leerTexto("Nombre completo: ");
        String licencia = ConsoleUtil.leerTexto("Tipo de licencia (ej: C2): ");
        String contacto = ConsoleUtil.leerTexto("Numero de contacto: ");

        Conductor conductor = new Conductor(identificacion, nombre, licencia, contacto);
        Conductor creado = controller.registrarConductor(conductor, usuarioOperador);
        System.out.println(">> Conductor registrado con id #" + creado.getIdConductor());
        ConsoleUtil.pausar();
    }

    private void listarTodos() {
        ConsoleUtil.imprimirEncabezado("Listado de conductores");
        List<Conductor> conductores = controller.listarTodos();
        if (conductores.isEmpty()) {
            System.out.println("No hay conductores registrados.");
        } else {
            conductores.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void listarPorEstado() {
        ConsoleUtil.imprimirEncabezado("Listar conductores por estado");
        System.out.println("1. Activo  2. De Vacaciones  3. Inactivo");
        int op = ConsoleUtil.leerOpcion("Seleccione: ", 1, 3);
        EstadoConductor estado = switch (op) {
            case 1 -> EstadoConductor.ACTIVO;
            case 2 -> EstadoConductor.DE_VACACIONES;
            default -> EstadoConductor.INACTIVO;
        };
        List<Conductor> conductores = controller.listarPorEstado(estado);
        if (conductores.isEmpty()) {
            System.out.println("No hay conductores en estado " + estado + ".");
        } else {
            conductores.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void actualizarEstado() {
        ConsoleUtil.imprimirEncabezado("Actualizar estado de conductor");
        int id = ConsoleUtil.leerEntero("Id del conductor: ");
        System.out.println("1. Activo  2. De Vacaciones  3. Inactivo");
        int op = ConsoleUtil.leerOpcion("Nuevo estado: ", 1, 3);
        EstadoConductor estado = switch (op) {
            case 1 -> EstadoConductor.ACTIVO;
            case 2 -> EstadoConductor.DE_VACACIONES;
            default -> EstadoConductor.INACTIVO;
        };
        boolean ok = controller.actualizarEstado(id, estado, usuarioOperador);
        System.out.println(ok ? ">> Estado actualizado." : ">> No se encontro el conductor indicado.");
        ConsoleUtil.pausar();
    }

    private void asignarAVehiculo() {
        ConsoleUtil.imprimirEncabezado("Asignar conductor a vehiculo disponible");
        int idConductor = ConsoleUtil.leerEntero("Id del conductor: ");
        int idVehiculo = ConsoleUtil.leerEntero("Id del vehiculo: ");
        String resultado = controller.asignarConductorAVehiculo(idConductor, idVehiculo, usuarioOperador);
        System.out.println(">> " + resultado);
        ConsoleUtil.pausar();
    }

    private void verAsignaciones() {
        ConsoleUtil.imprimirEncabezado("Historial de asignaciones vehiculo-conductor");
        controller.listarAsignaciones().forEach(System.out::println);
        ConsoleUtil.pausar();
    }
}
