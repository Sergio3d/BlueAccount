package com.cumn.blueaccount;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cumn.blueaccount.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.Objects;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String Api_Base_URL = "https://api.exchangerate.host/latest";

    private ActivityMainBinding binding;
    final static String LOG_TAG = "blueaccount";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static String grupoActual;

    public static String getGrupoActual() {
        return grupoActual;
    }

    public static void setGrupoActual(String grupoActual) {
        MainActivity.grupoActual = grupoActual;
    }

    private static final int RC_SIGN_IN = 2022;

    private TextView texto;
    private Button boton;
    private String inicioGrupo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        //AUTH
        findViewById(R.id.logoutButton).setOnClickListener(this);
        mFirebaseAuth = FirebaseAuth.getInstance();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    CharSequence username = user.getDisplayName();
                    Toast.makeText(MainActivity.this, getString(R.string.firebase_user_fmt, username), Toast.LENGTH_LONG).show();
                    Log.i(LOG_TAG, "onAuthStateChanged() " + getString(R.string.firebase_user_fmt, username));

                    user = Objects.requireNonNull(mFirebaseAuth.getCurrentUser());
                    if(grupoActual==null) {
                        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.rutaPreferences), Context.MODE_PRIVATE);
                        grupoActual = sharedPref.getString("grupoActual", null);
                        if(grupoActual==null) {
                            BuscarGrupoInicial(user, sharedPref);
                        }
                    }
                } else {
                    // user is signed out
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app
                            AuthUI.getInstance().
                                    createSignInIntentBuilder().
                                    setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    )).
                                    setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                                    //setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                                            build(),
                            RC_SIGN_IN

                    );
                    //Remains logged in when unique gmail account
                    // setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                    //setIsSmartLockEnabled(BuildConfig.DEBUG /* credentials */, true /* hints */).

                }
            }
        };

        //boton cambio de activity
        boton = findViewById(R.id.Moneda);
        texto= findViewById(R.id.TextoMoneda);
        boton.setOnClickListener(v ->{
            Intent i = new Intent(MainActivity.this.getBaseContext(), Cambio_Divisas.class);
            startActivity(i);
        });

        //Mostrar divisa
        float numero=Seleccionado.GlobalVariables.getValor();
        String result = Seleccionado.GlobalVariables.myString.substring(0, Seleccionado.GlobalVariables.myString.indexOf(":")); // Obtiene la subcadena desde el inicio hasta ":"
        texto.setText(result);
        setContentView(binding.getRoot());
    }

    private void BuscarGrupoInicial(FirebaseUser user, SharedPreferences sharedPref) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blueaccount-e4707-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("/Grupos/");
        myRef.get().addOnCompleteListener((OnCompleteListener<DataSnapshot>) task -> {
            for (DataSnapshot child : task.getResult().getChildren()) {
                if (child.child("NombreGrupo").getValue().toString().equals(user.getDisplayName())) {
                    if (child.child("Usarios").getChildrenCount() == 1 && child.child("Usarios").getValue().toString().contains(user.getEmail())) {
                        grupoActual = child.getKey();

                    }
                }
            }
        });
        if(grupoActual==null){
            DatabaseReference newgrupo = myRef.push();
            newgrupo.child("NombreGrupo").setValue(user.getDisplayName());
            newgrupo.child("Usuarios").push().setValue(user.getEmail());
            grupoActual = newgrupo.getKey();
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("grupoActual", grupoActual);
        editor.apply();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.rutaPreferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("grupoActual", getGrupoActual());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.rutaPreferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("grupoActual", getGrupoActual());
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.signed_in, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_in));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.signed_cancelled, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_cancelled));
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.rutaPreferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("grupoActual", null);
        editor.apply();
        grupoActual=null;
        mFirebaseAuth.signOut();
        Log.i(LOG_TAG, getString(R.string.signed_out));
    }
}

