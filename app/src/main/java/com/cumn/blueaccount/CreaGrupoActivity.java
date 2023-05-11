package com.cumn.blueaccount;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CreaGrupoActivity extends AppCompatActivity {

    private EditText nombreGrupo, textMiembros;
    private Button creaGrupo, cancelar;
    private ArrayList<String> usuarios;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creagrupo);



        //getActionBar().setDisplayHomeAsUpEnabled(true);
        textMiembros = findViewById(R.id.inputMiembros);
        nombreGrupo = findViewById(R.id.idNombreGrupo);
        creaGrupo = findViewById(R.id.bCrearGrupo);
        creaGrupo.setOnClickListener(v ->{

            ArrayList<String> newmiembros = new ArrayList<String>(Arrays.asList(textMiembros.getText().toString().split("\\s*[;, \n]\\s*")));
            ArrayList<String> miembros = new ArrayList<>();
            String nGrupo = nombreGrupo.getText().toString();

            if (!newmiembros.isEmpty() && !nGrupo.isEmpty()) {

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://blueaccount-e4707-default-rtdb.europe-west1.firebasedatabase.app");
                DatabaseReference newGrupo = database.getReference("/Grupos/").push();
                newGrupo.child("NombreGrupo").setValue(nGrupo);
                DatabaseReference usuarioRef = newGrupo.child("Usuarios");

                FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
                String myUser = firebaseauth.getCurrentUser().getEmail();
                if(!newmiembros.contains(myUser)){
                    newmiembros.add(myUser);
                }

                for (String user : newmiembros) {
                    firebaseauth.fetchSignInMethodsForEmail(user).addOnCompleteListener((OnCompleteListener<SignInMethodQueryResult>) task -> {
                        if (task.isSuccessful()) {

                            if (task.getResult().getSignInMethods().size() == 0) {
                                Toast toast = Toast.makeText(this.getBaseContext(), user + " no existe en la App", Toast.LENGTH_LONG);
                                toast.show();
                            } else if (!miembros.contains(user)) {
                                newGrupo.child("Usuarios").push().setValue(user);
                                miembros.add(user);
                            }
                            /*SharedPreferences sharedPref = CreaGrupoActivity.this.getSharedPreferences(getString(R.string.rutaPreferences), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("grupoActual", nombreGrupo.getText().toString());
                            editor.apply();*/
                            MainActivity.setGrupoActual(newGrupo.getKey());
                            volverMain();

                        } else {
                            Toast toast = Toast.makeText(this.getBaseContext(), "Error", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
            }

        });

        cancelar = findViewById(R.id.bCrearGrupoCancelar);
        cancelar.setOnClickListener(v ->{
            volverMain();
        });
    }

    private ArrayList<String> getMiembros(DatabaseReference usuarioRef) {
        ArrayList<String> miembros = new ArrayList<>();
        usuarioRef.orderByKey()
                .get()
                .addOnCompleteListener((OnCompleteListener<DataSnapshot>) task -> {
                    if (task.isSuccessful()) {
                        for (DataSnapshot child : task.getResult().getChildren()) {
                            miembros.add(Objects.requireNonNull(child.getValue()).toString());
                        }
                    } else {
                        Toast toast = Toast.makeText(this.getBaseContext(), "Error", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
        return miembros;
    }

    private void volverMain() {
        Intent creagrupo = new Intent(CreaGrupoActivity.this.getBaseContext(), MainActivity.class);
        startActivity(creagrupo);
    }
}
