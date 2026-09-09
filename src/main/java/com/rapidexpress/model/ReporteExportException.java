package com.rapidexpress.model;

/**
 * Excepcion personalizada (checked) para errores al exportar reportes a
 * archivos de texto. Se usa en ReporteFileExporter y se propaga hasta la
 * vista, donde se captura para mostrar un mensaje amigable sin caer el
 * programa.
 */
public class ReporteExportException extends Exception {

    public ReporteExportException(String mensaje) {
        super(mensaje);
    }

    public ReporteExportException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
