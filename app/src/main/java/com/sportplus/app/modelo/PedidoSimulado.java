package com.sportplus.app.modelo;

import java.util.List;

/** Copia del carrito y de los datos de contacto al confirmar. Solo existe en memoria. */
public final class PedidoSimulado {

    private static PedidoSimulado actual;

    public final List<Carrito.Linea> lineas;
    public final int subtotal;
    public final int envio;
    public final int total;
    public final String nombre;
    public final String correo;
    public final String ciudad;

    private PedidoSimulado(Carrito carrito, String nombre, String correo, String ciudad) {
        this.lineas = carrito.lineas();
        this.subtotal = carrito.subtotal();
        this.envio = carrito.envio();
        this.total = carrito.total();
        this.nombre = nombre;
        this.correo = correo;
        this.ciudad = ciudad;
    }

    public static PedidoSimulado crear(Carrito carrito, String nombre, String correo, String ciudad) {
        actual = new PedidoSimulado(carrito, nombre, correo, ciudad);
        return actual;
    }

    public static PedidoSimulado actual() {
        return actual;
    }
}
