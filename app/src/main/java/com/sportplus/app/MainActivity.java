package com.sportplus.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.StringRes;
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
 * Las pantallas de nivel 2 y 3 se apilan encima y muestran flecha de regreso.
 */
public class MainActivity extends AppCompatActivity implements Carrito.Observador {

    private BottomNavigationView navegacionInferior;
    private View divisorNavegacion;
    private ImageButton botonAtras;
    private ImageButton botonCarrito;
    private TextView tituloBarra;
    private View logo;
    private TextView contadorCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navegacionInferior = findViewById(R.id.bottomNav);
        divisorNavegacion = findViewById(R.id.dividerNav);
        botonAtras = findViewById(R.id.btnAtras);
        botonCarrito = findViewById(R.id.btnCarrito);
        tituloBarra = findViewById(R.id.tvTituloBarra);
        logo = findViewById(R.id.logoContainer);
        contadorCarrito = findViewById(R.id.tvContadorCarrito);

        navegacionInferior.setOnItemSelectedListener(item -> {
            mostrarSeccion(item.getItemId());
            return true;
        });
        botonCarrito.setOnClickListener(v -> irA(R.id.nav_carrito));
        botonAtras.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        getSupportFragmentManager().addOnBackStackChangedListener(this::sincronizarBarra);

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

    /** Va a una seccion principal, cierre o no pantallas apiladas, y la marca en la barra inferior. */
    public void irA(int destinoId) {
        if (navegacionInferior.getSelectedItemId() == destinoId) {
            mostrarSeccion(destinoId);
        } else {
            navegacionInferior.setSelectedItemId(destinoId);
        }
    }

    /** Apila una pantalla de nivel 2 o 3 con su titulo en la barra superior. */
    public void abrirPantalla(Fragment pantalla, @StringRes int titulo) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, pantalla)
                .addToBackStack(String.valueOf(titulo))
                .commit();
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

    private void sincronizarBarra() {
        FragmentManager manager = getSupportFragmentManager();
        int apiladas = manager.getBackStackEntryCount();
        if (apiladas == 0) {
            aplicarBarra(false, 0);
        } else {
            String nombre = manager.getBackStackEntryAt(apiladas - 1).getName();
            aplicarBarra(true, Integer.parseInt(nombre));
        }
    }

    private void aplicarBarra(boolean secundaria, @StringRes int titulo) {
        botonAtras.setVisibility(secundaria ? View.VISIBLE : View.GONE);
        tituloBarra.setVisibility(secundaria ? View.VISIBLE : View.GONE);
        logo.setVisibility(secundaria ? View.GONE : View.VISIBLE);
        navegacionInferior.setVisibility(secundaria ? View.GONE : View.VISIBLE);
        divisorNavegacion.setVisibility(secundaria ? View.GONE : View.VISIBLE);
        if (secundaria) {
            tituloBarra.setText(titulo);
        }
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
