package com.cumn.blueaccount.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cumn.blueaccount.MainActivity;
import com.cumn.blueaccount.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TransacAdapter extends RecyclerView.Adapter<TransacAdapter.ViewHolder> {

    private ArrayList<TransacEntity> listaTransac;

    public TransacAdapter(ArrayList<TransacEntity> listaTransac) {
        this.listaTransac = listaTransac;
    }

    @NonNull
    @Override
    public TransacAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemtransac,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransacAdapter.ViewHolder holder, int position) {
        holder.itemCant.setText(listaTransac.get(position).getCANTIDAD());
        holder.itemDesc.setText(listaTransac.get(position).getDESCRIP());
        holder.itemFecha.setText(listaTransac.get(position).getFECHA());
        holder.itemUser.setText(listaTransac.get(position).getUSER());
        holder.bBorrar.setOnClickListener(v -> {
            String grupo = MainActivity.getGrupoActual();
            String cuenta = listaTransac.get(position).getID();
            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://blueaccount-e4707-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference myRef = database.getReference("/Grupos/"+ grupo + "/Cuentas/" + cuenta);
            myRef.removeValue();
        });
    }

    @Override
    public int getItemCount() {
        return listaTransac.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCant, itemDesc, itemUser, itemFecha;
        private Button bBorrar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCant = itemView.findViewById(R.id.textCantItem);
            itemDesc = itemView.findViewById(R.id.textDesc);
            itemFecha = itemView.findViewById(R.id.textFechaItem);
            itemUser = itemView.findViewById(R.id.textUser);
            bBorrar = itemView.findViewById(R.id.buttonBorrarItem);
        }
    }
}
