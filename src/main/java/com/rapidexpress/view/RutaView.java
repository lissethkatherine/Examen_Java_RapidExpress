package com.rapidexpress.view;

import com.rapidexpress.controller.RutaController;
import com.rapidexpress.model.EstadoRuta;
import com.rapidexpress.model.Ruta;
import com.rapidexpress.model.RutaPaquete;
import com.rapidexpress.util.ConsoleUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RutaView {

    private final RutaController controller = new RutaController();
    private final String usuarioOperador;

    public RutaView(String usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            ConsoleUtil.imprimirEncabezado("PLANIFICACION Y SEGUIMIENTO DE RUTAS");
            System.out.println("1. Crear hoja de ruta");
            System.out.println("2. Iniciar ruta (Planificada -> En Curso)");
            System.out.println("3. Actualizar entrega de un paquete en ruta activa");
            System.out.println("4. Finalizar ruta (En Curso -> Finalizada)");
            System.out.println("5. Listar todas las rutas");
            System.out.println("6. Listar rutas por estado");
            System.out.println("7. Ver paquetes de una ruta");
            System.out.println("0. Volver al menu principal");
            opcion = ConsoleUtil.leerOpcion("Seleccione una opcion: ", 0, 7);

            switch (opcion) {
                case 1 -> crearHojaDeRuta();
                case 2 -> iniciarRuta();
                case 3 -> actualizarEntrega();
                case 4 -> finalizarRuta();
                case 5 -> listarTodas();
                case 6 -> listarPorEstado();
                case 7 -> verPaquetesDeRuta();
                case 0 -> System.out.println("Volviendo al menu principal...");
            }
        } while (opcion != 0);
    }

    private void crearHojaDeRuta() {
        ConsoleUtil.imprimirEncabezado("Crear hoja de ruta");
        int idVehiculo = ConsoleUtil.leerEntero("Id del vehiculo (debe estar Disponible): ");
        int idConductor = ConsoleUtil.leerEntero("Id del conductor (debe estar Activo): ");
        LocalDate fecha = ConsoleUtil.leerFecha("Fecha de la ruta");

        List<Integer> idsPaquetes = new ArrayList<>();
        System.out.println("Ingrese los ids de los paquetes a incluir (ingrese 0 para terminar):");
        while (true) {
            int id = ConsoleUtil.leerEntero("  Id de paquete (0 para terminar): ");
            if (id == 0) break;
            idsPaquetes.add(id);
        }

        String resultado = controller.crearHojaDeRuta(idVehiculo, idConductor, fecha, idsPaquetes, usuarioOperador);
        System.out.println(">> " + resultado);
        ConsoleUtil.pausar();
    }

    private void iniciarRuta() {
        ConsoleUtil.imprimirEncabezado("Iniciar ruta");
        int idRuta = ConsoleUtil.leerEntero("Id de la ruta: ");
        String resultado = controller.iniciarRuta(idRuta, usuarioOperador);
        System.out.println(">> " + resultado);
        ConsoleUtil.pausar();
    }

    private void actualizarEntrega() {
        ConsoleUtil.imprimirEncabezado("Actualizar entrega de paquete en ruta activa");
        int idRuta = ConsoleUtil.leerEntero("Id de la ruta: ");
        int idPaquete = ConsoleUtil.leerEntero("Id del paquete: ");
        System.out.println("1. Entregado  2. Devuelto  3. Pendiente");
        int op = ConsoleUtil.leerOpcion("Nuevo estado de entrega: ", 1, 3);
        String estado = switch (op) {
            case 1 -> "Entregado";
            case 2 -> "Devuelto";
            default -> "Pendiente";
        };
        String resultado = controller.actualizarEntregaPaquete(idRuta, idPaquete, estado, usuarioOperador);
        System.out.println(">> " + resultado);
        ConsoleUtil.pausar();
    }

    private void finalizarRuta() {
        ConsoleUtil.imprimirEncabezado("Finalizar ruta");
        int idRuta = ConsoleUtil.leerEntero("Id de la ruta: ");
        String resultado = controller.finalizarRuta(idRuta, usuarioOperador);
        System.out.println(">> " + resultado);
        ConsoleUtil.pausar();
    }

    private void listarTodas() {
        ConsoleUtil.imprimirEncabezado("Listado de rutas");
        List<Ruta> rutas = controller.listarTodas();
        if (rutas.isEmpty()) {
            System.out.println("No hay rutas registradas.");
        } else {
            rutas.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void listarPorEstado() {
        ConsoleUtil.imprimirEncabezado("Listar rutas por estado");
        System.out.println("1. Planificada  2. En Curso  3. Finalizada");
        int op = ConsoleUtil.leerOpcion("Seleccione: ", 1, 3);
        EstadoRuta estado = switch (op) {
            case 1 -> EstadoRuta.PLANIFICADA;
            case 2 -> EstadoRuta.EN_CURSO;
            default -> EstadoRuta.FINALIZADA;
        };
        List<Ruta> rutas = controller.listarPorEstado(estado);
        if (rutas.isEmpty()) {
            System.out.println("No hay rutas en estado " + estado + ".");
        } else {
            rutas.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void verPaquetesDeRuta() {
        ConsoleUtil.imprimirEncabezado("Paquetes de una ruta");
        int idRuta = ConsoleUtil.leerEntero("Id de la ruta: ");
        List<RutaPaquete> paquetes = controller.listarPaquetesDeRuta(idRuta);
        if (paquetes.isEmpty()) {
            System.out.println("Esta ruta no tiene paquetes asignados.");
        } else {
            paquetes.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }
}
