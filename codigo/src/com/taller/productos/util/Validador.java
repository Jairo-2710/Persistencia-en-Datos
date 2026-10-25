package com.taller.productos.util;

import com.taller.productos.modelo.Producto;

import java.math.BigDecimal;

/* Validacion de datos antes de enviarlos a la base de datos*/
public final class Validador {

    private Validador() {
    }

    /* Convierte los textos del formulario en un Producto válido y tambien se usaron los @throws ValidacionException por si algún dato es vacio o incorrecto*/
    public static Producto construirProducto(String codigo, String nombre, String descripcion,
                                             String precioTxt, String cantidadTxt) throws ValidacionException {
        codigo = codigo == null ? "" : codigo.trim();
        nombre = nombre == null ? "" : nombre.trim();
        descripcion = descripcion == null ? "" : descripcion.trim();
        precioTxt = precioTxt == null ? "" : precioTxt.trim();
        cantidadTxt = cantidadTxt == null ? "" : cantidadTxt.trim();

        if (codigo.isEmpty()) {
            throw new ValidacionException("El código es obligatorio.");
        }
        if (codigo.length() > 20) {
            throw new ValidacionException("El código no puede superar 20 caracteres.");
        }
        if (nombre.isEmpty()) {
            throw new ValidacionException("El nombre es obligatorio.");
        }
        if (nombre.length() > 100) {
            throw new ValidacionException("El nombre no puede superar 100 caracteres.");
        }
        if (descripcion.length() > 255) {
            throw new ValidacionException("La descripción no puede superar 255 caracteres.");
        }

        BigDecimal precio = validarPrecio(precioTxt);
        int cantidad = validarCantidad(cantidadTxt);

        return new Producto(codigo, nombre, descripcion, precio, cantidad);
    }

    public static BigDecimal validarPrecio(String texto) throws ValidacionException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ValidacionException("El precio es obligatorio.");
        }
        BigDecimal precio;
        try {
            precio = new BigDecimal(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("El precio debe ser un número válido.");
        }
        if (precio.signum() < 0) {
            throw new ValidacionException("El precio no puede ser negativo.");
        }
        return precio;
    }

    public static int validarCantidad(String texto) throws ValidacionException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ValidacionException("La cantidad es obligatoria.");
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("La cantidad debe ser un número entero.");
        }
        if (cantidad < 0) {
            throw new ValidacionException("La cantidad no puede ser negativa.");
        }
        return cantidad;
    }
}
