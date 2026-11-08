package com.sportplus.app.modelo;

import android.content.Context;

import com.sportplus.app.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Catalogo fijo de cuatro productos de demostracion. */
public final class Catalogo {

    private static final List<Producto> PRODUCTOS = Collections.unmodifiableList(Arrays.asList(
            new Producto(1, R.string.producto_zapatillas_nombre, R.string.producto_zapatillas_descripcion,
                    389900, R.drawable.img_producto_zapatillas, R.string.cd_img_zapatillas),
            new Producto(2, R.string.producto_camisa_nombre, R.string.producto_camisa_descripcion,
                    99900, R.drawable.img_producto_camisa, R.string.cd_img_camisa),
            new Producto(3, R.string.producto_bandas_nombre, R.string.producto_bandas_descripcion,
                    89900, R.drawable.img_producto_bandas, R.string.cd_img_bandas),
            new Producto(4, R.string.producto_botella_nombre, R.string.producto_botella_descripcion,
                    39900, R.drawable.img_producto_botella, R.string.cd_img_botella)));

    private Catalogo() {
    }

    public static List<Producto> todos() {
        return PRODUCTOS;
    }

    /** Los dos primeros productos se muestran como destacados en la pantalla de inicio. */
    public static List<Producto> destacados() {
        return PRODUCTOS.subList(0, 2);
    }

    public static Producto porId(int id) {
        for (Producto p : PRODUCTOS) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }

    /** Busca por nombre o descripcion, sin distinguir mayusculas ni tildes. */
    public static List<Producto> buscar(Context contexto, String consulta) {
        String q = normalizar(consulta);
        if (q.isEmpty()) {
            return PRODUCTOS;
        }
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : PRODUCTOS) {
            String texto = normalizar(contexto.getString(p.nombreRes) + " " + contexto.getString(p.descripcionRes));
            if (texto.contains(q)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    private static String normalizar(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase(Locale.ROOT).trim();
    }
}
