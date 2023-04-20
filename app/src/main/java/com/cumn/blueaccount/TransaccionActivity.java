package com.cumn.blueaccount;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;

public class TransaccionActivity extends AppCompatActivity{

    /*public static final String PARAM_IDFECHA = "com.cumn.blueaccount.idfecha";
    public static final Boolean PARAM_GAS_ING = Boolean.valueOf("com.cumn.blueaccount.gas_ing");
    public static final String PARAM_DESCRIP = "com.cumn.blueaccount.descripcion";
    public static final String PARAM_ETIQUETA = "com.cumn.blueaccount.etiqueta";
    public static final Float PARAM_CANTIDAD = Float.valueOf("com.cumn.blueaccount.cantidad");
    public static final Date PARAM_FECHA = Date.valueOf("com.cumn.blueaccount.fecha");*/

    private RadioButton GastoButton, IngresoButton;
    private Button createButton;
    private EditText inputCantidad, inputFecha, inputDescripcion;
    private AutoCompleteTextView inputEtiqueta;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_nuevo);

        Intent myIntent = getIntent();

        Log.i("BAcc", "Se crea la vista Nuevo");

        GastoButton  = findViewById(R.id.GastoButton);
        IngresoButton = findViewById(R.id.IngresoButton);
        createButton = findViewById(R.id.createButton);
        inputCantidad = findViewById(R.id.inputCantidad);
        inputFecha = findViewById(R.id.inputFecha);
        inputDescripcion = findViewById(R.id.inputDescripcion);
        inputEtiqueta = findViewById(R.id.inputEtiqueta);

        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> {

            Intent replyIntent = new Intent();
            Log.i("BAcc", "Boton CREAR apretado");
            if ((TextUtils.isEmpty(GastoButton.getText()) || TextUtils.isEmpty(IngresoButton.getText())) && TextUtils.isEmpty(inputCantidad.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                float cantidad;
                if (GastoButton.isChecked()) {
                    cantidad = 0-Float.parseFloat(inputCantidad.getText().toString().replace(",","."));
                }else{
                    cantidad = Float.parseFloat(inputCantidad.getText().toString().replace(",","."));
                }

                String etiqueta = inputEtiqueta.getText().toString();

                String descripcion = inputDescripcion.getText().toString();

                String fechaConvertida = null;

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if(TextUtils.isEmpty(inputFecha.getText())){
                    Calendar calendar = Calendar.getInstance();
                    fechaConvertida = dateFormat.format(calendar.getTime());
                }else {
                    Date parsed = null;
                    try {
                        parsed = dateFormat.parse(inputFecha.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    fechaConvertida = new Date(parsed.getTime()).toString();
                }

                String cadena = cantidad+";"+etiqueta+";"+descripcion+";"+fechaConvertida;
                try {
                    FileOutputStream fos =openFileOutput( "librocuentas.csv", Context.MODE_APPEND);
                    Log.i("BAcc", "Fichero librocuentas.csv generado");
                    fos.write(cadena.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK, replyIntent);

            }
            finish();
        });


    }

}
