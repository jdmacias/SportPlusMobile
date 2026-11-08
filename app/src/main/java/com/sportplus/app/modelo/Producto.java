package com.sportplus.app.modelo;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/** Producto de demostracion. El precio se expresa en pesos colombianos (COP) sin decimales. */
public final class Producto {

    public final int id;
    @StringRes public final int nombreRes;
    @StringRes public final int descripcionRes;
    public final int precio;
    @DrawableRes public final int imagenRes;
    @StringRes public final int descripcionImagenRes;

    public Producto(int id, @StringRes int nombreRes, @StringRes int descripcionRes, int precio,
                    @DrawableRes int imagenRes, @StringRes int descripcionImagenRes) {
        this.id = id;
        this.nombreRes = nombreRes;
        this.descripcionRes = descripcionRes;
        this.precio = precio;
        this.imagenRes = imagenRes;
        this.descripcionImagenRes = descripcionImagenRes;
    }
}
