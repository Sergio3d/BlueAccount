package com.cumn.blueaccount;

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

import java.sql.Date;
import java.text.ParseException;

public class TransaccionActivity extends AppCompatActivity {

    static String LOG_TAG = "BlueAccount";
    //public static final String PARAM_IDFECHA = "com.cumn.blueaccount.idfecha";
    public static final Boolean PARAM_GAS_ING = Boolean.valueOf("com.cumn.blueaccount.gas_ing");
    public static final String PARAM_DESCRIP = "com.cumn.blueaccount.descripcion";
    public static final String PARAM_ETIQUETA = "com.cumn.blueaccount.etiqueta";
    public static final Float PARAM_CANTIDAD = Float.valueOf("com.cumn.blueaccount.cantidad");
    public static final Date PARAM_FECHA = Date.valueOf("com.cumn.blueaccount.fecha");

    private RadioButton GastoButton, IngresoButton;
    private Button createButton;
    private EditText inputCantidad, inputFecha, inputDescripcion;
    private AutoCompleteTextView inputEtiqueta;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_nuevo);

        Intent myIntent = getIntent();


        GastoButton  = findViewById(R.id.GastoButton);
        IngresoButton = findViewById(R.id.IngresoButton);
        createButton = findViewById(R.id.createButton);
        inputCantidad = findViewById(R.id.inputCantidad);
        inputFecha = findViewById(R.id.inputFecha);
        inputDescripcion = findViewById(R.id.inputDescripcion);
        inputEtiqueta = findViewById(R.id.inputEtiqueta);

        final Button button = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if ((TextUtils.isEmpty(GastoButton.getText()) || TextUtils.isEmpty(IngresoButton.getText())) && TextUtils.isEmpty(inputCantidad.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {

                    float cantidad = Float.parseFloat(inputCantidad.getText().toString().replace(",","."));
                    Boolean gas_ing = GastoButton.isChecked();
                    gas_ing = IngresoButton.isChecked();
                    String etiqueta = inputEtiqueta.getText().toString();
                    String descripcion = inputDescripcion.getText().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    java.sql.Date fechaConvertida=null;
                    Date parsed = null;
                    try {
                        parsed = (Date) dateFormat.parse(inputFecha.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    fechaConvertida = new java.sql.Date(parsed.getTime());
                    setResult(RESULT_OK, replyIntent);

                }
                finish();
            }
        });


    }
}
