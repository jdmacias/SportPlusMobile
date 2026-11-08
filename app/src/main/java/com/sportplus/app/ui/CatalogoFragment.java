package com.sportplus.app.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportplus.app.R;
import com.sportplus.app.modelo.Carrito;
import com.sportplus.app.modelo.Catalogo;
import com.sportplus.app.modelo.Producto;

import java.util.List;

/** Catalogo con buscador por nombre o descripcion y estado de busqueda sin resultados. */
public class CatalogoFragment extends Fragment {

    private ProductoAdapter adaptador;
    private RecyclerView lista;
    private View sinResultados;
    private TextView contador;

    public CatalogoFragment() {
        super(R.layout.fragment_catalogo);
    }

    @Override
    public void onViewCreated(@NonNull View vista, @Nullable Bundle estado) {
        super.onViewCreated(vista, estado);

        EditText buscador = vista.findViewById(R.id.etBuscar);
        contador = vista.findViewById(R.id.tvContador);
        lista = vista.findViewById(R.id.rvCatalogo);
        sinResultados = vista.findViewById(R.id.cardSinResultados);

        lista.setLayoutManager(new GridLayoutManager(requireContext(),
                getResources().getInteger(R.integer.catalogo_columnas)));
        adaptador = new ProductoAdapter(Catalogo.todos(), R.string.boton_agregar_carrito,
                producto -> Carrito.get().agregar(producto.id, 1));
        lista.setAdapter(adaptador);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int inicio, int cuenta, int despues) {
            }

            @Override
            public void onTextChanged(CharSequence s, int inicio, int antes, int cuenta) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });
        vista.findViewById(R.id.btnVerTodos).setOnClickListener(v -> buscador.setText(""));

        filtrar(buscador.getText().toString());
    }

    private void filtrar(String consulta) {
        List<Producto> resultado = Catalogo.buscar(requireContext(), consulta);
        adaptador.actualizar(resultado);

        boolean hayConsulta = !consulta.trim().isEmpty();
        int plural = hayConsulta ? R.plurals.catalogo_encontrados : R.plurals.catalogo_disponibles;
        contador.setText(getResources().getQuantityString(plural, resultado.size(), resultado.size()));

        lista.setVisibility(resultado.isEmpty() ? View.GONE : View.VISIBLE);
        sinResultados.setVisibility(resultado.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
