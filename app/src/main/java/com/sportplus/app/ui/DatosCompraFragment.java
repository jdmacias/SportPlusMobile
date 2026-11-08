package com.sportplus.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sportplus.app.MainActivity;
import com.sportplus.app.R;
import com.sportplus.app.modelo.Carrito;
import com.sportplus.app.modelo.Formato;
import com.sportplus.app.modelo.PedidoSimulado;

import java.util.ArrayList;
import java.util.List;

/** Formulario de cinco campos con validacion por campo y resumen de la compra simulada. */
public class DatosCompraFragment extends Fragment {

    private interface Regla {
        boolean cumple(String texto);
    }

    private static final class Campo {
        final EditText entrada;
        final TextView error;
        final Regla regla;

        Campo(EditText entrada, TextView error, Regla regla) {
            this.entrada = entrada;
            this.error = error;
            this.regla = regla;
        }

        String texto() {
            return entrada.getText().toString().trim();
        }

        boolean esValido() {
            return regla.cumple(texto());
        }

        void marcar(boolean conError) {
            entrada.setBackgroundResource(conError ? R.drawable.bg_field_error : R.drawable.bg_field);
            error.setVisibility(conError ? View.VISIBLE : View.GONE);
        }
    }

    private final List<Campo> campos = new ArrayList<>();
    private TextView avisoErrores;

    public DatosCompraFragment() {
        super(R.layout.fragment_datos_compra);
    }

    @Override
    public void onViewCreated(@NonNull View vista, @Nullable Bundle estado) {
        super.onViewCreated(vista, estado);
        campos.clear();

        agregarCampo(vista, R.id.etNombre, R.id.tvErrorNombre, texto -> texto.length() >= 3);
        agregarCampo(vista, R.id.etCorreo, R.id.tvErrorCorreo,
                texto -> Patterns.EMAIL_ADDRESS.matcher(texto).matches());
        agregarCampo(vista, R.id.etTelefono, R.id.tvErrorTelefono,
                texto -> texto.replaceAll("\\D", "").length() >= 7);
        agregarCampo(vista, R.id.etDireccion, R.id.tvErrorDireccion, texto -> texto.length() >= 5);
        agregarCampo(vista, R.id.etCiudad, R.id.tvErrorCiudad, texto -> texto.length() >= 3);

        avisoErrores = vista.findViewById(R.id.tvAvisoErrores);

        Carrito carrito = Carrito.get();
        ((TextView) vista.findViewById(R.id.tvDatosUnidades)).setText(
                getResources().getQuantityString(R.plurals.unidades_cantidad, carrito.unidades(), carrito.unidades()));
        ((TextView) vista.findViewById(R.id.tvDatosTotal)).setText(Formato.cop(carrito.total()));

        vista.findViewById(R.id.btnConfirmar).setOnClickListener(v -> confirmar(vista));
        vista.findViewById(R.id.btnVolverCarrito).setOnClickListener(
                v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    private void agregarCampo(View vista, int idEntrada, int idError, Regla regla) {
        Campo campo = new Campo(vista.findViewById(idEntrada), vista.findViewById(idError), regla);
        campo.entrada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int inicio, int cuenta, int despues) {
            }

            @Override
            public void onTextChanged(CharSequence s, int inicio, int antes, int cuenta) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (campo.error.getVisibility() == View.VISIBLE && campo.esValido()) {
                    campo.marcar(false);
                    actualizarAviso();
                }
            }
        });
        campos.add(campo);
    }

    private int contarErrores() {
        int errores = 0;
        for (Campo campo : campos) {
            if (campo.error.getVisibility() == View.VISIBLE) {
                errores++;
            }
        }
        return errores;
    }

    private void actualizarAviso() {
        int errores = contarErrores();
        if (errores == 0) {
            avisoErrores.setVisibility(View.GONE);
        } else {
            avisoErrores.setText(getResources().getQuantityString(R.plurals.aviso_errores_cantidad, errores, errores));
        }
    }

    private void confirmar(View vista) {
        EditText primerInvalido = null;
        for (Campo campo : campos) {
            boolean invalido = !campo.esValido();
            campo.marcar(invalido);
            if (invalido && primerInvalido == null) {
                primerInvalido = campo.entrada;
            }
        }

        if (primerInvalido != null) {
            avisoErrores.setVisibility(View.VISIBLE);
            actualizarAviso();
            primerInvalido.requestFocus();
            return;
        }

        avisoErrores.setVisibility(View.GONE);
        InputMethodManager teclado = requireContext().getSystemService(InputMethodManager.class);
        if (teclado != null) {
            teclado.hideSoftInputFromWindow(vista.getWindowToken(), 0);
        }
        PedidoSimulado.crear(Carrito.get(), campos.get(0).texto(), campos.get(1).texto(), campos.get(4).texto());
        Carrito.get().vaciar();
        ((MainActivity) requireActivity()).abrirPantalla(new ConfirmacionFragment(), R.string.titulo_confirmacion);
    }
}
