package com.taller.productos.util;

/* Se uso ValidacionException para cuando los datos ingresados por el usuario no son válidos.*/
public class ValidacionException extends Exception {

    private static final long serialVersionUID = 1L;

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
