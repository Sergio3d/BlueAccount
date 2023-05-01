package com.cumn.blueaccount;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreaGrupoActivity extends AppCompatActivity {

    private EditText nombreGrupo, textMiembros;
    private Button creaGrupo;
    private ArrayList<String> usuarios;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blueaccount-e4707-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("/Grupos/");



        getActionBar().setDisplayHomeAsUpEnabled(true);
        nombreGrupo = findViewById(R.id.idNombreGrupo);
        creaGrupo = findViewById(R.id.bCreaGrupo);
        creaGrupo.setOnClickListener(v ->{

         /*   // Start listing users from the beginning, 1000 at a time.
            ListUsersPage page = null;
            try {
                FirebaseApp defaultApp = FirebaseApp.initializeApp();
                page = FirebaseAuth.getInstance(defaultApp).listUsers(null);
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
            while (page != null) {
                for (ExportedUserRecord user : page.getValues()) {
                    usuarios.add(user.getDisplayName());
                }
                page = page.getNextPage();
            }

            // Defining two MultiAutoCompleteTextView
            // This is to recognize comma separated.
            MultiAutoCompleteTextView multiAutoCompleteTextViewDefault;

            // This is the second one and required for custom features
            MultiAutoCompleteTextView multiAutoCompleteTextViewCustom;

            DatabaseReference newGrupo = myRef.child(nombreGrupo.getText().toString());
            multiAutoCompleteTextViewDefault = findViewById(R.id.idMiembrosGrupo);

            // In order to show the substring options in a dropdown, we need ArrayAdapter
            // and here it is using simple_list_item_1
            ArrayAdapter<String> randomArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,usuarios);
            multiAutoCompleteTextViewDefault.setAdapter(randomArrayAdapter);

            // setThreshold() is used to specify the number of characters after which
            // the dropdown with the autocomplete suggestions list would be displayed.
            // For multiAutoCompleteTextViewDefault, after 1 character, the dropdown shows substring
            multiAutoCompleteTextViewDefault.setThreshold(1);

            // Default CommaTokenizer is used here
            multiAutoCompleteTextViewDefault.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            */

            DatabaseReference newGrupo = myRef.child(nombreGrupo.getText().toString());

            newGrupo.child("Cuentas");
            List<String> miembros = Arrays.asList(textMiembros.getText().toString().split("\\s*,\\s*"));
            for (String user : miembros ) {
                newGrupo.child("Usuarios").push().setValue(user);
            }

        });

    }

    void checkEmailExistsOrNot(){
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        firebaseauth.fetchSignInMethodsForEmail(textMiembros.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().size() == 0){
                    // email not existed
                }else {
                    // email existed
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
}
