package com.rapidexpress.util;

import com.rapidexpress.model.ReporteExportException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad de persistencia en archivos: exporta el contenido de un reporte
 * (ya construido como texto, tipicamente con Stream API) a un archivo .txt
 * en disco. Es una funcionalidad nueva e independiente -no modifica ni
 * reemplaza a AuditoriaService, que sigue escribiendo auditoria.log igual
 * que antes.
 */
public final class ReporteFileExporter {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private ReporteFileExporter() {
    }

    /**
     * Escribe el contenido dado a un archivo de texto nuevo dentro de la
     * carpeta "reportes/" (se crea si no existe). Devuelve la ruta final
     * del archivo generado.
     *
     * @throws ReporteExportException si ocurre cualquier error de E/S,
     *         envolviendo la IOException original para que la vista pueda
     *         mostrar un mensaje claro sin exponer detalles tecnicos crudos.
     */
    public static String exportar(String nombreBase, String contenido) throws ReporteExportException {
        String marcaTiempo = LocalDateTime.now().format(FORMATO);
        String nombreArchivo = "reportes/" + nombreBase + "_" + marcaTiempo + ".txt";

        try {
            java.io.File carpeta = new java.io.File("reportes");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            try (FileWriter fw = new FileWriter(nombreArchivo);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println("=".repeat(60));
                pw.println("RapidExpress - Reporte generado el " + LocalDateTime.now());
                pw.println("=".repeat(60));
                pw.println();
                pw.print(contenido);
            }

            return nombreArchivo;
        } catch (IOException e) {
            throw new ReporteExportException(
                    "No se pudo escribir el archivo de reporte en '" + nombreArchivo + "'", e);
        }
    }
}
