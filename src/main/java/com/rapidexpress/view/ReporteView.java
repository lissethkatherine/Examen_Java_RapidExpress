package com.rapidexpress.view;

import com.rapidexpress.controller.ReporteController;
import com.rapidexpress.model.DetalleEntregaPaquete;
import com.rapidexpress.model.RegistroAuditoria;
import com.rapidexpress.model.Ruta;
import com.rapidexpress.util.ConsoleUtil;

import java.time.LocalDate;
import java.util.List;

public class ReporteView {

    private final ReporteController controller = new ReporteController();

    public void mostrarMenu() {
        int opcion;
        do {
            ConsoleUtil.imprimirEncabezado("REPORTES Y AUDITORIA");
            System.out.println("1. Entregas de un conductor en un rango de fechas");
            System.out.println("2. Historial de rutas de un vehiculo");
            System.out.println("3. Consultar bitacora de auditoria");
            System.out.println("4. Exportar reporte de entregas a archivo de texto");
            System.out.println("0. Volver al menu principal");
            opcion = ConsoleUtil.leerOpcion("Seleccione una opcion: ", 0, 4);

            switch (opcion) {
                case 1 -> entregasPorConductor();
                case 2 -> historialVehiculo();
                case 3 -> verAuditoria();
                case 4 -> exportarReporteEntregas();
                case 0 -> System.out.println("Volviendo al menu principal...");
            }
        } while (opcion != 0);
    }

    private void entregasPorConductor() {
        ConsoleUtil.imprimirEncabezado("Entregas por conductor en rango de fechas (detalle por paquete)");
        int idConductor = ConsoleUtil.leerEntero("Id del conductor: ");
        LocalDate desde = ConsoleUtil.leerFecha("Desde");
        LocalDate hasta = ConsoleUtil.leerFecha("Hasta");
        List<DetalleEntregaPaquete> detalle = controller.entregasPorConductor(idConductor, desde, hasta);
        if (detalle.isEmpty()) {
            System.out.println("No se encontraron paquetes transportados por ese conductor en el rango indicado.");
        } else {
            detalle.forEach(System.out::println);
            long entregados = detalle.stream().filter(d -> "Entregado".equals(d.getEstadoEntrega())).count();
            long devueltos = detalle.stream().filter(d -> "Devuelto".equals(d.getEstadoEntrega())).count();
            long pendientes = detalle.size() - entregados - devueltos;
            System.out.printf("%nTotal: %d paquete(s) - Entregados: %d - Devueltos: %d - Pendientes: %d%n",
                    detalle.size(), entregados, devueltos, pendientes);
        }
        ConsoleUtil.pausar();
    }

    private void historialVehiculo() {
        ConsoleUtil.imprimirEncabezado("Historial de rutas de un vehiculo");
        int idVehiculo = ConsoleUtil.leerEntero("Id del vehiculo: ");
        List<Ruta> rutas = controller.historialRutasDeVehiculo(idVehiculo);
        if (rutas.isEmpty()) {
            System.out.println("Este vehiculo no tiene rutas registradas.");
        } else {
            rutas.forEach(System.out::println);
        }
        ConsoleUtil.pausar();
    }

    private void verAuditoria() {
        ConsoleUtil.imprimirEncabezado("Bitacora de auditoria (ultimos registros)");
        List<RegistroAuditoria> registros = controller.consultarAuditoria();
        registros.stream().limit(30).forEach(System.out::println);
        System.out.println("\nNota: el registro completo tambien se guarda en el archivo auditoria.log");
        ConsoleUtil.pausar();
    }

    
    private void exportarReporteEntregas() {
        ConsoleUtil.imprimirEncabezado("Exportar reporte de entregas a archivo");
        int idConductor = ConsoleUtil.leerEntero("Id del conductor: ");
        LocalDate desde = ConsoleUtil.leerFecha("Desde");
        LocalDate hasta = ConsoleUtil.leerFecha("Hasta");

        List<DetalleEntregaPaquete> detalle = controller.entregasPorConductor(idConductor, desde, hasta);

        try {
            String rutaArchivo = controller.exportarReporteEntregas(idConductor, detalle);
            System.out.println(">> Reporte exportado exitosamente en: " + rutaArchivo);
            System.out.println();
            System.out.println(controller.estadisticasEntregas(detalle));
        } catch (com.rapidexpress.model.ReporteExportException e) {
            System.out.println(">> No se pudo exportar el reporte: " + e.getMessage());
            System.out.println(">> Verifique permisos de escritura en la carpeta del proyecto.");
        }
        ConsoleUtil.pausar();
    }
}
