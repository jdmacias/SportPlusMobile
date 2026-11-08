package com.sportplus.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sportplus.app.modelo.Carrito;
import com.sportplus.app.ui.CarritoFragment;
import com.sportplus.app.ui.CatalogoFragment;
import com.sportplus.app.ui.InicioFragment;

/**
 * Actividad principal: barra superior con el carrito, contenedor de pantallas
 * y barra de navegacion inferior con tres destinos (Inicio, Catalogo y Carrito).
 */
public class MainActivity extends AppCompatActivity implements Carrito.Observador {

    private BottomNavigationView navegacionInferior;
    private ImageButton botonCarrito;
    private TextView contadorCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navegacionInferior = findViewById(R.id.bottomNav);
        botonCarrito = findViewById(R.id.btnCarrito);
        contadorCarrito = findViewById(R.id.tvContadorCarrito);

        navegacionInferior.setOnItemSelectedListener(item -> {
            mostrarSeccion(item.getItemId());
            return true;
        });
        botonCarrito.setOnClickListener(v -> irA(R.id.nav_carrito));

        if (savedInstanceState == null) {
            mostrarSeccion(R.id.nav_inicio);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Carrito.get().agregarObservador(this);
        actualizarContador();
    }

    @Override
    protected void onStop() {
        Carrito.get().quitarObservador(this);
        super.onStop();
    }

    @Override
    public void alCambiarCarrito() {
        actualizarContador();
    }

    /** Cambia de seccion principal y marca el destino en la barra inferior. */
    public void irA(int destinoId) {
        navegacionInferior.setSelectedItemId(destinoId);
    }

    private void mostrarSeccion(int destinoId) {
        Fragment pantalla;
        if (destinoId == R.id.nav_inicio) {
            pantalla = new InicioFragment();
        } else if (destinoId == R.id.nav_catalogo) {
            pantalla = new CatalogoFragment();
        } else {
            pantalla = new CarritoFragment();
        }
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        manager.beginTransaction().replace(R.id.fragmentContainer, pantalla).commit();
    }

    private void actualizarContador() {
        int unidades = Carrito.get().unidades();
        if (unidades > 0) {
            contadorCarrito.setText(unidades > 99 ? "99+" : String.valueOf(unidades));
            contadorCarrito.setVisibility(View.VISIBLE);
            botonCarrito.setContentDescription(
                    getResources().getQuantityString(R.plurals.cd_carrito_cantidad, unidades, unidades));
        } else {
            contadorCarrito.setVisibility(View.GONE);
            botonCarrito.setContentDescription(getString(R.string.cd_carrito));
        }
    }
}
