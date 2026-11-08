package com.sportplus.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sportplus.app.MainActivity;
import com.sportplus.app.R;
import com.sportplus.app.modelo.Carrito;
import com.sportplus.app.modelo.Formato;
import com.sportplus.app.modelo.PedidoSimulado;

/** Confirmacion de la compra simulada: aclara que no hubo compra real y resume el pedido. */
public class ConfirmacionFragment extends Fragment {

    public ConfirmacionFragment() {
        super(R.layout.fragment_confirmacion);
    }

    @Override
    public void onViewCreated(@NonNull View vista, @Nullable Bundle estado) {
        super.onViewCreated(vista, estado);
        MainActivity actividad = (MainActivity) requireActivity();

        // Desde la confirmacion, regresar lleva al inicio (el carrito ya esta vacio).
        actividad.getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                actividad.irA(R.id.nav_inicio);
            }
        });
        vista.findViewById(R.id.btnSeguirExplorando).setOnClickListener(v -> actividad.irA(R.id.nav_catalogo));
        vista.findViewById(R.id.btnVolverInicio).setOnClickListener(v -> actividad.irA(R.id.nav_inicio));

        PedidoSimulado pedido = PedidoSimulado.actual();
        if (pedido == null) {
            actividad.irA(R.id.nav_inicio);
            return;
        }

        LinearLayout contenedor = vista.findViewById(R.id.layoutLineasPedido);
        LayoutInflater inflador = LayoutInflater.from(requireContext());
        for (Carrito.Linea linea : pedido.lineas) {
            View fila = inflador.inflate(R.layout.item_pedido_linea, contenedor, false);
            String descripcion = getString(R.string.pedido_linea, linea.cantidad, getString(linea.producto.nombreRes));
            ((TextView) fila.findViewById(R.id.tvLineaDescripcion)).setText(descripcion);
            ((TextView) fila.findViewById(R.id.tvLineaValor)).setText(Formato.cop(linea.subtotal()));
            contenedor.addView(fila);
        }

        ((TextView) vista.findViewById(R.id.tvConfSubtotal)).setText(Formato.cop(pedido.subtotal));
        ((TextView) vista.findViewById(R.id.tvConfEnvio)).setText(Formato.cop(pedido.envio));
        ((TextView) vista.findViewById(R.id.tvConfTotal)).setText(Formato.cop(pedido.total));
        ((TextView) vista.findViewById(R.id.tvConfNombre)).setText(pedido.nombre);
        ((TextView) vista.findViewById(R.id.tvConfCorreo)).setText(pedido.correo);
        ((TextView) vista.findViewById(R.id.tvConfCiudad)).setText(pedido.ciudad);
    }
}
