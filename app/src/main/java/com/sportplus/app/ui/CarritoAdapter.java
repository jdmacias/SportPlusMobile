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
import androidx.recyclerview.widget.RecyclerView;

import com.sportplus.app.R;
import com.sportplus.app.modelo.Carrito;
import com.sportplus.app.modelo.Formato;
import com.sportplus.app.modelo.Producto;

import java.util.ArrayList;
import java.util.List;

/** Muestra las lineas del carrito (item_carrito) con su selector de cantidad. */
public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.Holder> {

    public interface Acciones {
        void cambiarCantidad(int productoId, int nuevaCantidad);

        void eliminar(int productoId);
    }

    private final List<Carrito.Linea> lineas = new ArrayList<>();
    private final Acciones acciones;

    public CarritoAdapter(Acciones acciones) {
        this.acciones = acciones;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void actualizar(List<Carrito.Linea> nuevas) {
        lineas.clear();
        lineas.addAll(nuevas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        View vista = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_carrito, padre, false);
        return new Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int posicion) {
        Carrito.Linea linea = lineas.get(posicion);
        Producto producto = linea.producto;
        Context contexto = holder.itemView.getContext();
        String nombre = contexto.getString(producto.nombreRes);

        holder.imagen.setImageResource(producto.imagenRes);
        holder.imagen.setContentDescription(contexto.getString(producto.descripcionImagenRes));
        holder.nombre.setText(producto.nombreRes);
        holder.subtotal.setText(contexto.getString(R.string.subtotal_linea, Formato.cop(linea.subtotal())));
        holder.cantidad.setText(String.valueOf(linea.cantidad));

        holder.menos.setContentDescription(contexto.getString(R.string.cd_menos_producto, nombre));
        holder.mas.setContentDescription(contexto.getString(R.string.cd_mas_producto, nombre));
        holder.eliminar.setContentDescription(contexto.getString(R.string.cd_eliminar_producto, nombre));

        holder.menos.setOnClickListener(v -> acciones.cambiarCantidad(producto.id, linea.cantidad - 1));
        holder.mas.setOnClickListener(v -> acciones.cambiarCantidad(producto.id, linea.cantidad + 1));
        holder.eliminar.setOnClickListener(v -> acciones.eliminar(producto.id));
    }

    @Override
    public int getItemCount() {
        return lineas.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView imagen;
        final TextView nombre;
        final TextView subtotal;
        final TextView cantidad;
        final Button menos;
        final Button mas;
        final Button eliminar;

        Holder(@NonNull View vista) {
            super(vista);
            imagen = vista.findViewById(R.id.ivProductoCarrito);
            nombre = vista.findViewById(R.id.tvNombreCarrito);
            subtotal = vista.findViewById(R.id.tvSubtotalLinea);
            cantidad = vista.findViewById(R.id.tvCantidad);
            menos = vista.findViewById(R.id.btnMenos);
            mas = vista.findViewById(R.id.btnMas);
            eliminar = vista.findViewById(R.id.btnEliminar);
        }
    }
}
