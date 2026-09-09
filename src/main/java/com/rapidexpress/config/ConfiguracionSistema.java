package com.rapidexpress.config;

/**
 * Patron de diseno Singleton: garantiza que exista una unica instancia de
 * la configuracion general del sistema (nombre, version) durante toda la
 * ejecucion del programa. Es una clase nueva e independiente -no reemplaza
 * a DatabaseConfig, que sigue funcionando exactamente igual que antes.
 *
 * Uso: ConfiguracionSistema.getInstancia().getNombreSistema()
 */
public class ConfiguracionSistema {

    private static ConfiguracionSistema instancia;

    private final String nombreSistema;
    private final String version;

    // Constructor privado: nadie puede hacer "new ConfiguracionSistema()"
    // desde afuera, solo esta misma clase puede crear su unica instancia.
    private ConfiguracionSistema() {
        this.nombreSistema = "RapidExpress - Sistema de Gestion de Flotas y Rutas";
        this.version = "1.0.0";
    }

    /**
     * Punto de acceso unico a la instancia. La primera vez que se llama,
     * crea el objeto; las siguientes veces, siempre devuelve el mismo.
     */
    public static ConfiguracionSistema getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionSistema();
        }
        return instancia;
    }

    public String getNombreSistema() {
        return nombreSistema;
    }

    public String getVersion() {
        return version;
    }
}
