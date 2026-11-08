package com.sportplus.app.modelo;

import java.text.NumberFormat;
import java.util.Locale;

/** Formato de precios en pesos colombianos: $389.900. */
public final class Formato {

    private Formato() {
    }

    public static String cop(long valor) {
        NumberFormat formato = NumberFormat.getIntegerInstance(new Locale("es", "CO"));
        formato.setGroupingUsed(true);
        return "$" + formato.format(valor);
    }
}
