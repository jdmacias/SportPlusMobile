package com.sportplus.app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportplus.app.MainActivity;
import com.sportplus.app.R;
import com.sportplus.app.modelo.Carrito;
import com.sportplus.app.modelo.Formato;

/** Carrito con sus lineas, resumen de valores y estado vacio. */
public class CarritoFragment extends Fragment implements Carrito.Observador {

    private CarritoAdapter adaptador;
    private View contenido;
    private View vacio;
    private TextView unidades;
    private TextView subtotal;
    private TextView envio;
    private TextView total;

    public CarritoFragment() {
        super(R.layout.fragment_carrito);
    }

    @Override
    public void onViewCreated(@NonNull View vista, @Nullable Bundle estado) {
        super.onViewCreated(vista, estado);

        contenido = vista.findViewById(R.id.layoutContenido);
        vacio = vista.findViewById(R.id.cardVacio);
        unidades = vista.findViewById(R.id.tvResumenUnidades);
        subtotal = vista.findViewById(R.id.tvResumenSubtotal);
        envio = vista.findViewById(R.id.tvResumenEnvio);
        total = vista.findViewById(R.id.tvResumenTotal);

        adaptador = new CarritoAdapter(new CarritoAdapter.Acciones() {
            @Override
            public void cambiarCantidad(int productoId, int nuevaCantidad) {
                Carrito.get().cambiarCantidad(productoId, nuevaCantidad);
            }

            @Override
            public void eliminar(int productoId) {
                Carrito.get().eliminar(productoId);
            }
        });
        RecyclerView lineas = vista.findViewById(R.id.rvLineas);
        lineas.setLayoutManager(new LinearLayoutManager(requireContext()));
        lineas.setAdapter(adaptador);

        View.OnClickListener irAlCatalogo = v -> ((MainActivity) requireActivity()).irA(R.id.nav_catalogo);
        vista.findViewById(R.id.btnSeguirComprando).setOnClickListener(irAlCatalogo);
        vista.findViewById(R.id.btnIrCatalogo).setOnClickListener(irAlCatalogo);
        vista.findViewById(R.id.btnVaciar).setOnClickListener(v -> Carrito.get().vaciar());

        refrescar();
    }

    @Override
    public void onStart() {
        super.onStart();
        Carrito.get().agregarObservador(this);
        refrescar();
    }

    @Override
    public void onStop() {
        Carrito.get().quitarObservador(this);
        super.onStop();
    }

    @Override
    public void alCambiarCarrito() {
        refrescar();
    }

    private void refrescar() {
        if (getView() == null) {
            return;
        }
        Carrito carrito = Carrito.get();
        boolean sinProductos = carrito.estaVacio();
        contenido.setVisibility(sinProductos ? View.GONE : View.VISIBLE);
        vacio.setVisibility(sinProductos ? View.VISIBLE : View.GONE);

        adaptador.actualizar(carrito.lineas());
        unidades.setText(String.valueOf(carrito.unidades()));
        subtotal.setText(Formato.cop(carrito.subtotal()));
        envio.setText(Formato.cop(carrito.envio()));
        total.setText(Formato.cop(carrito.total()));
    }
}
