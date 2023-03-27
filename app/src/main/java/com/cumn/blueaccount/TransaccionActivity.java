package com.cumn.blueaccount;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;

public class TransaccionActivity extends AppCompatActivity {

    public static final String PARAM_IDFECHA = "com.cumn.blueaccount.idfecha";
    public static final Boolean PARAM_GAS_ING = Boolean.valueOf("com.cumn.blueaccount.gas_ing");
    public static final String PARAM_DESCRIP = "com.cumn.blueaccount.descripcion";
    public static final String PARAM_ETIQUETA = "com.cumn.blueaccount.etiqueta";
    public static final Float PARAM_CANTIDAD = Float.valueOf("com.cumn.blueaccount.cantidad");
    public static final Date PARAM_FECHA = Date.valueOf("com.cumn.blueaccount.fecha");

    private RadioButton GastoButton, IngresoButton;
    private Button createButton;
    private EditText inputCantidad, inputFecha;
    private AutoCompleteTextView inputEtiqueta;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_nuevo);
    }
}
