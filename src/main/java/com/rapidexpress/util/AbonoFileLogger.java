package com.rapidexpress.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Escribe cada abono registrado a un archivo de texto acumulativo
 * (abonos_multas.txt), usando try-with-resources para garantizar el
 * cierre del archivo incluso si ocurre un error de E/S.
 */
public final class AbonoFileLogger {

    private static final String ARCHIVO = "abonos_multas.txt";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AbonoFileLogger() {
    }

    /** @throws IOException si falla la escritura; quien llama decide como manejarlo. */
    public static void registrar(int idMulta, int idConductor, String descripcionPago,
                                  java.math.BigDecimal nuevoSaldo) throws IOException {
        try (FileWriter fw = new FileWriter(ARCHIVO, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.printf("[%s] Multa #%d (Conductor #%d) - %s - Nuevo saldo: $%,.0f%n",
                    LocalDateTime.now().format(FORMATO), idMulta, idConductor, descripcionPago, nuevoSaldo);
        }
    }
}
