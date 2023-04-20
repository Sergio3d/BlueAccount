package com.cumn.blueaccount;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cumn.blueaccount.databinding.ActivityMainBinding;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cumn.blueaccount.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView cantTotal;
    private RadioButton GastoButton, IngresoButton;
    private Button createButton;
    private EditText inputCantidad, inputFecha, inputDescripcion;
    private AutoCompleteTextView inputEtiqueta;
    final static String LOG_TAG = "btb";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final int RC_SIGN_IN = 2022;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);
        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            mFirebaseAuth.signOut();
            Log.i(LOG_TAG, getString(R.string.signed_out));
        });

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
                    ((TextView) findViewById(R.id.textView)).setText(getString(R.string.firebase_user_fmt, username));
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

        //Home

        setContentView(R.layout.fragment_home);


        Log.i("BAcc", "Se crea la vista Home");
        //final RecyclerView lista = (RecyclerView) findViewById(R.id.Lista);
        //final TransacListAdapter adapter = new TransacListAdapter(this);

        cantTotal = findViewById(R.id.cantTotal);
        cantTotal.setText(HomeActivity.getTotal());

        //Nuevo

        setContentView(R.layout.fragment_nuevo);

        botonCrear();
    }

    private void botonCrear() {
        //setContentView(R.layout.fragment_nuevo);
        GastoButton = findViewById(R.id.GastoButton);
        IngresoButton = findViewById(R.id.IngresoButton);
        createButton = findViewById(R.id.createButton);
        inputCantidad = findViewById(R.id.inputCantidad);
        inputFecha = findViewById(R.id.inputFecha);
        inputDescripcion = findViewById(R.id.inputDescripcion);
        inputEtiqueta = findViewById(R.id.inputEtiqueta);

        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(v -> {

            Intent replyIntent = new Intent();
            Log.i("BAcc", "Boton CREAR apretado");
            if ((TextUtils.isEmpty(GastoButton.getText()) || TextUtils.isEmpty(IngresoButton.getText())) && TextUtils.isEmpty(inputCantidad.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                float cantidad;
                if (GastoButton.isChecked()) {
                    cantidad = 0 - Float.parseFloat(inputCantidad.getText().toString().replace(",", "."));
                } else {
                    cantidad = Float.parseFloat(inputCantidad.getText().toString().replace(",", "."));
                }

                String etiqueta = inputEtiqueta.getText().toString();

                String descripcion = inputDescripcion.getText().toString();

                String fechaConvertida = null;

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if (TextUtils.isEmpty(inputFecha.getText())) {
                    Calendar calendar = Calendar.getInstance();
                    fechaConvertida = dateFormat.format(calendar.getTime());
                } else {
                    Date parsed = null;
                    try {
                        parsed = dateFormat.parse(inputFecha.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    assert parsed != null;
                    fechaConvertida = new Date(parsed.getTime()).toString();
                }

                String cadena = cantidad + ";" + etiqueta + ";" + descripcion + ";" + fechaConvertida;
                try {
                    FileOutputStream fos = openFileOutput("librocuentas.csv", Context.MODE_APPEND);
                    Log.i("BAcc", "Fichero librocuentas.csv generado");
                    fos.write(cadena.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK, replyIntent);

            }
        });
        //setContentView(binding.getRoot());
    }
    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
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
}