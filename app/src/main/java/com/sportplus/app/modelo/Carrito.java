package com.sportplus.app.modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Carrito de compras en memoria (sin persistencia). Avisa a los observadores cuando cambia. */
public final class Carrito {

    public static final int ENVIO_DEMOSTRATIVO = 15000;
    public static final int CANTIDAD_MAXIMA = 99;

    private static final Carrito INSTANCIA = new Carrito();

    public interface Observador {
        void alCambiarCarrito();
    }

    public static final class Linea {
        public final Producto producto;
        public final int cantidad;

        Linea(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public int subtotal() {
            return producto.precio * cantidad;
        }
    }

    private final LinkedHashMap<Integer, Integer> cantidades = new LinkedHashMap<>();
    private final List<Observador> observadores = new ArrayList<>();

    private Carrito() {
    }

    public static Carrito get() {
        return INSTANCIA;
    }

    public void agregarObservador(Observador observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void quitarObservador(Observador observador) {
        observadores.remove(observador);
    }

    public void agregar(int productoId, int cantidad) {
        if (Catalogo.porId(productoId) == null || cantidad <= 0) {
            return;
        }
        Integer actual = cantidades.get(productoId);
        int nueva = (actual == null ? 0 : actual) + cantidad;
        cantidades.put(productoId, Math.min(CANTIDAD_MAXIMA, nueva));
        notificar();
    }

    /** Cambia la cantidad de una linea. Valores menores que 1 se ignoran: para quitar se usa eliminar(). */
    public void cambiarCantidad(int productoId, int nueva) {
        if (!cantidades.containsKey(productoId) || nueva < 1) {
            return;
        }
        cantidades.put(productoId, Math.min(CANTIDAD_MAXIMA, nueva));
        notificar();
    }

    public void eliminar(int productoId) {
        if (cantidades.remove(productoId) != null) {
            notificar();
        }
    }

    public void vaciar() {
        if (!cantidades.isEmpty()) {
            cantidades.clear();
            notificar();
        }
    }

    public boolean estaVacio() {
        return cantidades.isEmpty();
    }

    public int unidades() {
        int total = 0;
        for (int cantidad : cantidades.values()) {
            total += cantidad;
        }
        return total;
    }

    public List<Linea> lineas() {
        List<Linea> lista = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : cantidades.entrySet()) {
            Producto p = Catalogo.porId(e.getKey());
            if (p != null) {
                lista.add(new Linea(p, e.getValue()));
            }
        }
        return lista;
    }

    public int subtotal() {
        int total = 0;
        for (Linea linea : lineas()) {
            total += linea.subtotal();
        }
        return total;
    }

    public int envio() {
        return estaVacio() ? 0 : ENVIO_DEMOSTRATIVO;
    }

    public int total() {
        return subtotal() + envio();
    }

    private void notificar() {
        for (Observador observador : new ArrayList<>(observadores)) {
            observador.alCambiarCarrito();
        }
    }
}
