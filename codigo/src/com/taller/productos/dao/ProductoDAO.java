package com.taller.productos.dao;

import com.taller.productos.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla productos (patrón DAO) sobre la base de datos H2 embebida.
 * Todas las consultas usan PreparedStatement y los recursos se cierran con try-with-resources.
 * Se usa SQL estándar, compatible con H2.
 */
public class ProductoDAO {

    private static final String COLUMNAS = "codigo, nombre, descripcion, precio, cantidad";

    private static final String SQL_INSERTAR =
            "INSERT INTO productos (" + COLUMNAS + ") VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_LISTAR =
            "SELECT " + COLUMNAS + " FROM productos ORDER BY codigo";
    // H2 distingue mayúsculas en LIKE; LOWER() hace la búsqueda insensible a mayúsculas.
    private static final String SQL_BUSCAR =
            "SELECT " + COLUMNAS + " FROM productos "
                    + "WHERE LOWER(codigo) LIKE ? OR LOWER(nombre) LIKE ? ORDER BY codigo";
    private static final String SQL_POR_CODIGO =
            "SELECT " + COLUMNAS + " FROM productos WHERE codigo = ?";
    private static final String SQL_ACTUALIZAR =
            "UPDATE productos SET descripcion = ?, precio = ?, cantidad = ? WHERE codigo = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM productos WHERE codigo = ?";

    /** Crea un producto. Devuelve true si se insertó una fila. */
    public boolean insertar(Producto p) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getCantidad());
            return ps.executeUpdate() > 0;
        }
    }

    /** Devuelve todos los productos. */
    public List<Producto> listar() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Busca por coincidencia parcial en código o nombre, sin distinguir mayúsculas. */
    public List<Producto> buscar(String texto) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String patron = "%" + texto.trim().toLowerCase() + "%";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    /** Devuelve el producto con ese código o null si no existe. */
    public Producto buscarPorCodigo(String codigo) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_POR_CODIGO)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /** Actualiza descripción, precio y cantidad. Devuelve true si el producto existía. */
    public boolean actualizar(Producto p) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, p.getDescripcion());
            ps.setBigDecimal(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            ps.setString(4, p.getCodigo());
            return ps.executeUpdate() > 0;
        }
    }

    /** Elimina por código. Devuelve true si el producto existía. */
    public boolean eliminar(String codigo) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBigDecimal("precio"),
                rs.getInt("cantidad"));
    }
}
