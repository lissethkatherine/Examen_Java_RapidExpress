package com.rapidexpress.view;

import com.rapidexpress.util.ConsoleUtil;

/** Menu principal del sistema: punto de entrada de la interfaz CLI. */
public class MenuPrincipalView {

    private final String usuarioOperador;

    public MenuPrincipalView(String usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
    }

    public void iniciar() {
        int opcion;
        do {
            ConsoleUtil.imprimirEncabezado("RAPIDEXPRESS - SISTEMA DE GESTION DE FLOTAS Y RUTAS");
            System.out.println("Usuario: " + usuarioOperador);
            System.out.println();
            System.out.println("1. Gestion de Flota de Vehiculos");
            System.out.println("2. Gestion de Personal (Conductores)");
            System.out.println("3. Gestion de Paquetes y Envios");
            System.out.println("4. Planificacion y Seguimiento de Rutas");
            System.out.println("5. Reportes y Auditoria");
            System.out.println("6. Gestion de Multas y Pagos de Conductores");
            System.out.println("0. Salir");
            opcion = ConsoleUtil.leerOpcion("Seleccione una opcion: ", 0, 6);

            switch (opcion) {
                case 1 -> new VehiculoView(usuarioOperador).mostrarMenu();
                case 2 -> new ConductorView(usuarioOperador).mostrarMenu();
                case 3 -> new PaqueteView(usuarioOperador).mostrarMenu();
                case 4 -> new RutaView(usuarioOperador).mostrarMenu();
                case 5 -> new ReporteView().mostrarMenu();
                case 6 -> new MultaConductorView(usuarioOperador).mostrarMenu();
                case 0 -> System.out.println("Hasta pronto.");
            }
        } while (opcion != 0);
    }
}
