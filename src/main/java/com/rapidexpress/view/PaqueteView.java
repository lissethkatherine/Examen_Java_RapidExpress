package com.rapidexpress.view;

import com.rapidexpress.controller.PaqueteController;
import com.rapidexpress.model.Cliente;
import com.rapidexpress.model.EstadoPaquete;
import com.rapidexpress.model.Paquete;
import com.rapidexpress.util.ConsoleUtil;

import java.math.BigDecimal;
import java.util.List;

public class PaqueteView {

    private final PaqueteController controller = new PaqueteController();
    private final String usuarioOperador;

    public PaqueteView(String usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            ConsoleUtil.imprimirEncabezado("GESTION DE PAQUETES Y ENVIOS");
            System.out.println("1. Registrar nuevo cliente (remitente/destinatario)");
            System.out.println("2. Listar clientes");
            System.out.println("3. Registrar nuevo paquete");
            System.out.println("4. Listar todos los paquetes");
            System.out.println("5. Listar paquetes por estado");
            System.out.println("6. Buscar paquete por codigo de seguimiento");
            System.out.println("7. Actualizar estado de un paquete");
            System.out.println("0. Volver al menu principal");
            opcion = ConsoleUtil.leerOpcion("Seleccione una opcion: ", 0, 7);

            switch (opcion) {
                case 1 -> registrarCliente();
                case 2 -> listarClientes();
                case 3 -> registrarPaquete();
                case 4 -> listarTodos();
                case 5 -> listarPorEstado();
                case 6 -> buscarPorCodigo();
                case 7 -> actualizarEstado();
                case 0 -> System.out.println("Volviendo al menu principal...");
            }
        } while (opcion != 0);
    }

    private void registrarCliente() {
        ConsoleUtil.imprimirEncabezado("Registrar cliente");
        String nombre = ConsoleUtil.leerTexto("Nombre completo: ");
        String tipoDoc = ConsoleUtil.leerTexto("Tipo de documento (ej: CC): ");
        String numDoc = ConsoleUtil.leerTexto("Numero de documento: ");
        String telefono = ConsoleUtil.leerTexto("Telefono: ");
        String email = ConsoleUtil.leerTexto("Email: ");
        String direccion = ConsoleUtil.leerTexto("Direccion: ");

        Cliente cliente = new Cliente(nombre, tipoDoc, numDoc, telefono, email, direccion);
        Cliente creado = controller.registrarCliente(cliente, usuarioOperador);
        System.out.println(">> Cliente registrado con id #" + creado.getIdCliente());
        ConsoleUtil.pausar();
    }

    private void listarClientes() {
        ConsoleUtil.imprimirEncabezado("Listado de clientes");
        List<Cliente> clientes = controller.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            clientes.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void registrarPaquete() {
        ConsoleUtil.imprimirEncabezado("Registrar nuevo paquete");
        String descripcion = ConsoleUtil.leerTexto("Descripcion del contenido: ");
        BigDecimal peso = ConsoleUtil.leerDecimal("Peso (kg): ");
        String dimensiones = ConsoleUtil.leerTexto("Dimensiones (ej: 30x20x15 cm): ");
        String origen = ConsoleUtil.leerTexto("Direccion de origen: ");
        String destino = ConsoleUtil.leerTexto("Direccion de destino: ");
        int idRemitente = ConsoleUtil.leerEntero("Id del cliente remitente: ");
        int idDestinatario = ConsoleUtil.leerEntero("Id del cliente destinatario: ");

        Paquete paquete = new Paquete(descripcion, peso, dimensiones, origen, destino,
                idRemitente, idDestinatario);
        Paquete creado = controller.registrarPaquete(paquete, usuarioOperador);
        System.out.println(">> Paquete registrado. Codigo de seguimiento: " + creado.getCodigoSeguimiento());
        ConsoleUtil.pausar();
    }

    private void listarTodos() {
        ConsoleUtil.imprimirEncabezado("Listado de paquetes");
        List<Paquete> paquetes = controller.listarTodos();
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes registrados.");
        } else {
            paquetes.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void listarPorEstado() {
        ConsoleUtil.imprimirEncabezado("Listar paquetes por estado");
        System.out.println("1. En Bodega  2. Asignado a Ruta  3. En Transito  4. Entregado  5. Devuelto");
        int op = ConsoleUtil.leerOpcion("Seleccione: ", 1, 5);
        EstadoPaquete estado = switch (op) {
            case 1 -> EstadoPaquete.EN_BODEGA;
            case 2 -> EstadoPaquete.ASIGNADO_A_RUTA;
            case 3 -> EstadoPaquete.EN_TRANSITO;
            case 4 -> EstadoPaquete.ENTREGADO;
            default -> EstadoPaquete.DEVUELTO;
        };
        List<Paquete> paquetes = controller.listarPorEstado(estado);
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes en estado " + estado + ".");
        } else {
            paquetes.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void buscarPorCodigo() {
        ConsoleUtil.imprimirEncabezado("Buscar paquete por codigo de seguimiento");
        String codigo = ConsoleUtil.leerTexto("Codigo de seguimiento (ej: RX-2026-000001): ");
        controller.buscarPorCodigo(codigo)
                .ifPresentOrElse(System.out::println, () -> System.out.println("No se encontro el paquete."));
        ConsoleUtil.pausar();
    }

    private void actualizarEstado() {
        ConsoleUtil.imprimirEncabezado("Actualizar estado de paquete");
        int id = ConsoleUtil.leerEntero("Id del paquete: ");
        System.out.println("1. En Bodega  2. Asignado a Ruta  3. En Transito  4. Entregado  5. Devuelto");
        int op = ConsoleUtil.leerOpcion("Nuevo estado: ", 1, 5);
        EstadoPaquete estado = switch (op) {
            case 1 -> EstadoPaquete.EN_BODEGA;
            case 2 -> EstadoPaquete.ASIGNADO_A_RUTA;
            case 3 -> EstadoPaquete.EN_TRANSITO;
            case 4 -> EstadoPaquete.ENTREGADO;
            default -> EstadoPaquete.DEVUELTO;
        };
        boolean ok = controller.actualizarEstado(id, estado, usuarioOperador);
        System.out.println(ok ? ">> Estado actualizado." : ">> No se encontro el paquete indicado.");
        ConsoleUtil.pausar();
    }
}
