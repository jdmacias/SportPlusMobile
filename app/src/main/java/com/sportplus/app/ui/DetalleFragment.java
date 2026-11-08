package com.sportplus.app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sportplus.app.MainActivity;
import com.sportplus.app.R;
import com.sportplus.app.modelo.Carrito;
import com.sportplus.app.modelo.Catalogo;
import com.sportplus.app.modelo.Formato;
import com.sportplus.app.modelo.Producto;

/** Ficha de un producto con selector de cantidad y boton para agregar al carrito. */
public class DetalleFragment extends Fragment {

    private static final String ARG_PRODUCTO_ID = "producto_id";

    private int cantidad = 1;
    private TextView valorCantidad;

    public DetalleFragment() {
        super(R.layout.fragment_detalle);
    }

    public static DetalleFragment nuevo(int productoId) {
        DetalleFragment fragmento = new DetalleFragment();
        Bundle argumentos = new Bundle();
        argumentos.putInt(ARG_PRODUCTO_ID, productoId);
        fragmento.setArguments(argumentos);
        return fragmento;
    }

    @Override
    public void onViewCreated(@NonNull View vista, @Nullable Bundle estado) {
        super.onViewCreated(vista, estado);

        Producto producto = Catalogo.porId(requireArguments().getInt(ARG_PRODUCTO_ID));
        if (producto == null) {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
            return;
        }

        ImageView imagen = vista.findViewById(R.id.ivDetalle);
        imagen.setImageResource(producto.imagenRes);
        imagen.setContentDescription(getString(producto.descripcionImagenRes));
        ((TextView) vista.findViewById(R.id.tvDetalleNombre)).setText(producto.nombreRes);
        ((TextView) vista.findViewById(R.id.tvDetallePrecio)).setText(Formato.cop(producto.precio));
        ((TextView) vista.findViewById(R.id.tvDetalleDescripcion)).setText(producto.descripcionRes);

        String nombre = getString(producto.nombreRes);
        View menos = vista.findViewById(R.id.btnDetalleMenos);
        View mas = vista.findViewById(R.id.btnDetalleMas);
        menos.setContentDescription(getString(R.string.cd_menos_producto, nombre));
        mas.setContentDescription(getString(R.string.cd_mas_producto, nombre));
        valorCantidad = vista.findViewById(R.id.tvDetalleCantidad);

        menos.setOnClickListener(v -> {
            if (cantidad > 1) {
                cantidad--;
                mostrarCantidad();
            }
        });
        mas.setOnClickListener(v -> {
            if (cantidad < Carrito.CANTIDAD_MAXIMA) {
                cantidad++;
                mostrarCantidad();
            }
        });

        View agregar = vista.findViewById(R.id.btnDetalleAgregar);
        agregar.setContentDescription(getString(R.string.cd_agregar_producto, nombre));
        agregar.setOnClickListener(v -> {
            Carrito.get().agregar(producto.id, cantidad);
            cantidad = 1;
            mostrarCantidad();
        });
        vista.findViewById(R.id.btnDetalleVolver)
                .setOnClickListener(v -> ((MainActivity) requireActivity()).irA(R.id.nav_catalogo));

        mostrarCantidad();
    }

    private void mostrarCantidad() {
        valorCantidad.setText(String.valueOf(cantidad));
    }
}
