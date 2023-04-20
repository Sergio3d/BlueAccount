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

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView cantTotal;
    private RadioButton GastoButton, IngresoButton;
    private Button createButton;
    private EditText inputCantidad, inputFecha, inputDescripcion;
    private AutoCompleteTextView inputEtiqueta;

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
}