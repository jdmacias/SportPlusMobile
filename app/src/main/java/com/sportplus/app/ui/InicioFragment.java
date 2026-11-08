package com.sportplus.app.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportplus.app.MainActivity;
import com.sportplus.app.R;
import com.sportplus.app.modelo.Carrito;
import com.sportplus.app.modelo.Catalogo;

/** Pantalla de inicio: aviso, banner y productos destacados. */
public class InicioFragment extends Fragment {

    public InicioFragment() {
        super(R.layout.fragment_inicio);
    }

    @Override
    public void onViewCreated(@NonNull View vista, @Nullable Bundle estado) {
        super.onViewCreated(vista, estado);

        vista.findViewById(R.id.btnVerCatalogo)
                .setOnClickListener(v -> ((MainActivity) requireActivity()).irA(R.id.nav_catalogo));

        RecyclerView destacados = vista.findViewById(R.id.rvDestacados);
        destacados.setLayoutManager(new GridLayoutManager(requireContext(),
                getResources().getInteger(R.integer.catalogo_columnas)));
        destacados.setAdapter(new ProductoAdapter(Catalogo.destacados(), R.string.boton_agregar,
                producto -> Carrito.get().agregar(producto.id, 1),
                producto -> ((MainActivity) requireActivity())
                        .abrirPantalla(DetalleFragment.nuevo(producto.id), R.string.titulo_producto)));
    }
}
