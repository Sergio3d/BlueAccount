package com.cumn.blueaccount;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cumn.blueaccount.models.TransacEntity;

import java.util.List;

public class TransacListAdapter extends RecyclerView.Adapter<TransacListAdapter.ItemViewHolder>{

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView cantidad;
        TextView etiqueta;
        TextView fecha;
        Button botonBorrar;
        public ItemViewHolder(View view) {
            super(view);
            cantidad = (TextView) view.findViewById(R.id.textCantItem);
            etiqueta = (TextView) view.findViewById(R.id.textEtiquetaItem);
            fecha = (TextView) view.findViewById(R.id.textFechaItem);
            botonBorrar = (Button) view.findViewById(R.id.buttonBorrarItem);
        }
    }

    private List<TransacEntity> listaItems;

    public TransacListAdapter(List<TransacEntity> listaItems) {
        this.listaItems = listaItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemtransac, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        TransacEntity current = listaItems.get(position);
        holder.cantidad.setText(current.getCANTIDAD().toString());
        holder.etiqueta.setText(current.getETIQUETA());
        holder.fecha.setText((CharSequence) current.getFECHA());

    }

    @Override
    public int getItemCount() {
        return (listaItems == null)
                ? 0
                : listaItems.size();
    }


}
