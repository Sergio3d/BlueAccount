package com.cumn.blueaccount;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class CambioDivisas extends AppCompatActivity {
    ExchangeRatesAPI api = new ExchangeRatesAPI();
    List<String> divisa = api.getDivisasDisponibles();

    //String[] divisas = {"USD","EUR","YEN"};
    String[] divisas = divisa.toArray(new String[divisa.size()]);
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_divisas);

        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this,R.layout.lista_divisas,divisas);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item"+item,Toast.LENGTH_SHORT).show();
            }
        });

        Button boton= findViewById(R.id.Volver);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intento);
            }
        });


    }
}