package com.taller.productos.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Administra la conexión JDBC con H2 en modo embebido (archivo local).
 * La base se guarda en la carpeta "data" junto al programa y la tabla
 * se crea automáticamente la primera vez que se ejecuta.
 */
public final class ConexionBD {

    private static final String USUARIO = "sa";
    private static final String CLAVE = "";

    private static final String SQL_CREAR_TABLA =
            "CREATE TABLE IF NOT EXISTS productos ("
                    + "codigo VARCHAR(20) NOT NULL, "
                    + "nombre VARCHAR(100) NOT NULL, "
                    + "descripcion VARCHAR(255), "
                    + "precio DECIMAL(10,2) NOT NULL, "
                    + "cantidad INT NOT NULL, "
                    + "CONSTRAINT pk_productos PRIMARY KEY (codigo), "
                    + "CONSTRAINT chk_precio CHECK (precio >= 0), "
                    + "CONSTRAINT chk_cantidad CHECK (cantidad >= 0))";

    private static final String URL = "jdbc:h2:" + rutaBase();
    private static boolean inicializada = false;

    private ConexionBD() {
    }

    /** Ruta del archivo de base de datos: carpeta "data" junto al .jar (o al directorio de trabajo). */
    private static String rutaBase() {
        File base;
        try {
            File origen = new File(ConexionBD.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            base = origen.isFile() ? origen.getParentFile() : new File(System.getProperty("user.dir"));
        } catch (Exception e) {
            base = new File(System.getProperty("user.dir"));
        }
        File datos = new File(base, "data");
        datos.mkdirs();
        return new File(datos, "productos").getAbsolutePath();
    }

    /*Abre una nueva conexión. Quien la solicita es responsable de cerrarla*/
    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el controlador JDBC de H2 (carpeta lib/).", e);
        }
        Connection con = DriverManager.getConnection(URL, USUARIO, CLAVE);
        inicializarTabla(con);
        return con;
    }

    private static synchronized void inicializarTabla(Connection con) throws SQLException {
        if (inicializada) {
            return;
        }
        try (Statement st = con.createStatement()) {
            st.execute(SQL_CREAR_TABLA);
        }
        inicializada = true;
    }
}
