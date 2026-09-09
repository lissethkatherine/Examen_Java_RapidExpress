package com.rapidexpress.controller;

import com.rapidexpress.dao.AuditoriaDAO;
import com.rapidexpress.dao.impl.AuditoriaDAOImpl;
import com.rapidexpress.model.DetalleEntregaPaquete;
import com.rapidexpress.model.RegistroAuditoria;
import com.rapidexpress.model.Ruta;

import java.time.LocalDate;
import java.util.List;

/**
 * Orquesta el modulo de Reportes y Auditoria: entregas por conductor en un
 * rango de fechas, historial de rutas de un vehiculo, y consulta de la
 * bitacora de auditoria.
 */
public class ReporteController {

    private final RutaController rutaController;
    private final AuditoriaDAO auditoriaDAO;

    public ReporteController() {
        this(new RutaController(), new AuditoriaDAOImpl());
    }

    public ReporteController(RutaController rutaController, AuditoriaDAO auditoriaDAO) {
        this.rutaController = rutaController;
        this.auditoriaDAO = auditoriaDAO;
    }

    /**
     * Reporte detallado (paquete por paquete) de las entregas realizadas
     * por un conductor en un rango de fechas.
     */
    public List<DetalleEntregaPaquete> entregasPorConductor(int idConductor, LocalDate desde, LocalDate hasta) {
        return rutaController.reporteDetalladoEntregasPorConductor(idConductor, desde, hasta);
    }

    /** Historial completo de rutas de un vehiculo. */
    public List<Ruta> historialRutasDeVehiculo(int idVehiculo) {
        return rutaController.listarPorVehiculo(idVehiculo);
    }

    public List<RegistroAuditoria> consultarAuditoria() {
        return auditoriaDAO.listarTodos();
    }

    /**
     * Genera estadisticas rapidas sobre un reporte de entregas usando
     * Stream API: cuantos paquetes fueron entregados, devueltos y estan
     * pendientes, mas el promedio y total de "operaciones" contabilizadas.
     * No reemplaza a entregasPorConductor(); se usa junto a el.
     */
    public String estadisticasEntregas(List<DetalleEntregaPaquete> detalle) {
        long entregados = detalle.stream()
                .filter(d -> "Entregado".equals(d.getEstadoEntrega()))
                .count();

        long devueltos = detalle.stream()
                .filter(d -> "Devuelto".equals(d.getEstadoEntrega()))
                .count();

        long pendientes = detalle.stream()
                .filter(d -> !"Entregado".equals(d.getEstadoEntrega()) && !"Devuelto".equals(d.getEstadoEntrega()))
                .count();

        double porcentajeExito = detalle.isEmpty()
                ? 0.0
                : (entregados * 100.0) / detalle.size();

        // Agrupa los paquetes por estado de entrega usando groupingBy + counting
        java.util.Map<String, Long> conteoPorEstado = detalle.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        DetalleEntregaPaquete::getEstadoEntrega,
                        java.util.stream.Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        sb.append("Total de paquetes analizados: ").append(detalle.size()).append("\n");
        sb.append("Entregados: ").append(entregados).append("\n");
        sb.append("Devueltos: ").append(devueltos).append("\n");
        sb.append("Pendientes: ").append(pendientes).append("\n");
        sb.append(String.format("Porcentaje de exito: %.1f%%\n", porcentajeExito));
        sb.append("Detalle por estado: ").append(conteoPorEstado).append("\n");

        return sb.toString();
    }

    /**
     * Construye el contenido de texto de un reporte de entregas (usando
     * Stream API para transformar cada fila) y lo exporta a un archivo
     * .txt dentro de la carpeta reportes/. Devuelve la ruta del archivo
     * generado.
     *
     * @throws com.rapidexpress.model.ReporteExportException si falla la
     *         escritura del archivo (se captura en la vista, sin caer el
     *         programa).
     */
    public String exportarReporteEntregas(int idConductor, List<DetalleEntregaPaquete> detalle)
            throws com.rapidexpress.model.ReporteExportException {

        String cuerpo = detalle.stream()
                .map(d -> String.format("Ruta #%d (%s) - Paquete #%d [%s] - %s -> Estado: %s",
                        d.getIdRuta(), d.getFechaRuta(), d.getIdPaquete(),
                        d.getCodigoSeguimiento(), d.getDireccionDestino(), d.getEstadoEntrega()))
                .collect(java.util.stream.Collectors.joining("\n"));

        String contenidoCompleto = "Reporte de entregas - Conductor #" + idConductor + "\n\n"
                + (cuerpo.isEmpty() ? "(sin paquetes en el rango de fechas indicado)\n" : cuerpo + "\n")
                + "\n" + estadisticasEntregas(detalle);

        return com.rapidexpress.util.ReporteFileExporter.exportar(
                "entregas_conductor_" + idConductor, contenidoCompleto);
    }
}
