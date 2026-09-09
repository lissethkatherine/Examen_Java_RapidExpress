package com.rapidexpress;

import com.rapidexpress.config.ConfiguracionSistema;
import com.rapidexpress.util.ConsoleUtil;
import com.rapidexpress.view.MenuPrincipalView;

/**
 * Punto de entrada del sistema de backend CLI "RapidExpress".
 *
 * El sistema se identifica con un usuario operador (usado para la bitacora
 * de auditoria) y despliega el menu principal, desde el cual se accede a
 * los 5 modulos funcionales: Flota, Personal, Paquetes, Rutas y Reportes.
 */
public class Main {

    public static void main(String[] args) {
        // ConfiguracionSistema es un Singleton: sin importar cuantas veces se
        // llame getInstancia(), siempre es el mismo objeto en toda la ejecucion.
        ConfiguracionSistema config = ConfiguracionSistema.getInstancia();
        System.out.println(config.getNombreSistema() + " v" + config.getVersion());

        System.out.println("Conectando con la base de datos de RapidExpress...");

        String usuarioOperador = ConsoleUtil.leerTexto("Identifiquese (usuario operador): ");
        if (usuarioOperador.isBlank()) {
            usuarioOperador = "operador.anonimo";
        }

        MenuPrincipalView menu = new MenuPrincipalView(usuarioOperador);
        menu.iniciar();
    }
}
