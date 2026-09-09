package com.rapidexpress.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de conexion a la base de datos, estilo simple (igual al que se ve
 * en clase con ConexionDB): guarda los datos de conexion y expone un
 * metodo estatico que devuelve la Connection lista para usar.
 *
 * @author RapidExpress
 */
public abstract class DatabaseConfig {

    // Datos de conexion a la base de datos MySQL en Aiven.
    // OJO: en un proyecto real esto no deberia ir hardcodeado aqui (es mejor
    // usar variables de entorno), pero lo dejamos simple como en clase.
    private static String url = "jdbc:mysql://mysql-3ebb1bdd-katherinecano020-f4b2.j.aivencloud.com:21896/rapidexpress_db?sslMode=REQUIRED";
    private static String user = "avnadmin";
    private static String password = "";

    public static Connection con = null;

    /**
     * Abre y devuelve la conexion a MySQL. Si algo sale mal, imprime el
     * error en consola y devuelve null (igual que en ConexionDB).
     */
    public static Connection getConnection() {
        con = null;
        try {
            con = DriverManager.getConnection(url, user, password);

            if (con != null) {
                DatabaseMetaData meta = con.getMetaData();
                System.out.println("Base de datos conectada: " + meta.getDriverName());
            }
        } catch (SQLException ex) {
            System.out.println("Error al conectar la BD: " + ex.getMessage());
        }
        return con;
    }
}
