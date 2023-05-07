package com.cumn.blueaccount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cumn.blueaccount.ui.home.HomeFragment;
import com.firebase.ui.auth.viewmodel.AuthViewModelBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class CambiarGrupoActivity extends AppCompatActivity {

    private ListView listaGrupos;
    private ArrayList<String> grupos, rutaGrupos;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiagrupo);
        listaGrupos = findViewById(R.id.listaGrupos);
        grupos = new ArrayList<String>();
        rutaGrupos = new ArrayList<String>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String user = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blueaccount-e4707-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("/Grupos/");
        myRef.get().addOnCompleteListener((OnCompleteListener<DataSnapshot>) task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot child : task.getResult().getChildren()) {
                    for (DataSnapshot member : child.child("Usuarios").getChildren()){
                        if(Objects.requireNonNull(member.getValue()).toString().equals(user)){
                            grupos.add(Objects.requireNonNull(child.child("NombreGrupo").getValue().toString()));
                            rutaGrupos.add(Objects.requireNonNull(child.getKey()));
                        }
                    }
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grupos);
                listaGrupos.setAdapter(adapter);
            } else {
                Toast toast = Toast.makeText(this.getBaseContext(), "Error", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        listaGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gruposelect = rutaGrupos.get(position);
                String nombre = grupos.get(position);
                /*SharedPreferences sharedPref = CambiarGrupoActivity.this.getSharedPreferences(getString(R.string.rutaPreferences), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("grupoActual", gruposelect);
                editor.apply();*/
                MainActivity.setGrupoActual(gruposelect);
                HomeFragment.setNameGrupo(nombre);
                //Toast toast = Toast.makeText(CambiarGrupoActivity.this.getBaseContext(), getString(R.string.grupoActual), Toast.LENGTH_LONG);
                //toast.show();
                Intent cambiagrupo = new Intent(CambiarGrupoActivity.this.getBaseContext(), MainActivity.class);
                startActivity(cambiagrupo);
            }
        });


    }


}
