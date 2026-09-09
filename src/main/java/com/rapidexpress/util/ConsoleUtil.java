package com.rapidexpress.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

/** Utilidades para la lectura y validacion de entradas por consola (CLI). */
public final class ConsoleUtil {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleUtil() {
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return SCANNER.nextLine().trim();
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(">> Por favor ingrese un numero entero valido.");
            }
        }
    }

    public static BigDecimal leerDecimal(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return new BigDecimal(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(">> Por favor ingrese un numero valido (ej: 1500.50).");
            }
        }
    }

    public static LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + " (formato yyyy-MM-dd): ");
                return LocalDate.parse(SCANNER.nextLine().trim());
            } catch (Exception e) {
                System.out.println(">> Formato de fecha invalido. Ejemplo: 2026-08-31");
            }
        }
    }

    public static int leerOpcion(String mensaje, int min, int max) {
        while (true) {
            int opcion = leerEntero(mensaje);
            if (opcion >= min && opcion <= max) {
                return opcion;
            }
            System.out.println(">> Opcion fuera de rango. Intente de nuevo.");
        }
    }

    public static void imprimirEncabezado(String titulo) {
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("  " + titulo);
        System.out.println("=".repeat(60));
    }

    public static void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        SCANNER.nextLine();
    }
}
