package com.sportplus.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.sportplus.app.R;
import com.sportplus.app.modelo.Formato;
import com.sportplus.app.modelo.Producto;

import java.util.ArrayList;
import java.util.List;

/** Muestra tarjetas de producto (item_producto) en una cuadricula. */
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.Holder> {

    public interface AlAgregar {
        void agregar(Producto producto);
    }

    public interface AlAbrir {
        void abrir(Producto producto);
    }

    private final List<Producto> datos = new ArrayList<>();
    private final int textoBoton;
    private final AlAgregar alAgregar;
    private final AlAbrir alAbrir;

    public ProductoAdapter(List<Producto> iniciales, @StringRes int textoBoton, AlAgregar alAgregar,
                           AlAbrir alAbrir) {
        this.datos.addAll(iniciales);
        this.textoBoton = textoBoton;
        this.alAgregar = alAgregar;
        this.alAbrir = alAbrir;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void actualizar(List<Producto> nuevos) {
        datos.clear();
        datos.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        View vista = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_producto, padre, false);
        return new Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int posicion) {
        Producto producto = datos.get(posicion);
        Context contexto = holder.itemView.getContext();
        String nombre = contexto.getString(producto.nombreRes);
        holder.imagen.setImageResource(producto.imagenRes);
        holder.imagen.setContentDescription(contexto.getString(producto.descripcionImagenRes));
        holder.nombre.setText(producto.nombreRes);
        holder.precio.setText(Formato.cop(producto.precio));
        holder.agregar.setText(textoBoton);
        holder.agregar.setContentDescription(contexto.getString(R.string.cd_agregar_producto, nombre));
        holder.agregar.setOnClickListener(v -> alAgregar.agregar(producto));
        holder.itemView.setOnClickListener(v -> alAbrir.abrir(producto));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView imagen;
        final TextView nombre;
        final TextView precio;
        final Button agregar;

        Holder(@NonNull View vista) {
            super(vista);
            imagen = vista.findViewById(R.id.ivProducto);
            nombre = vista.findViewById(R.id.tvNombreProducto);
            precio = vista.findViewById(R.id.tvPrecioProducto);
            agregar = vista.findViewById(R.id.btnAgregar);
        }
    }
}
