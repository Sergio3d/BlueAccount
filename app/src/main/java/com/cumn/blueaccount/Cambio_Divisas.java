package com.cumn.blueaccount;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Cambio_Divisas extends AppCompatActivity {

    public String[] divisas;
    Spinner spinner;
    TextView respuesta;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_divisas);

        Seleccionado.GlobalVariables.setValor(1);
        Seleccionado.GlobalVariables.setMyString("\"EUR\":1");


        Button btnActivity2 = findViewById(R.id.Cambiar);
        btnActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cambio_Divisas.this, MainActivity.class);
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://v6.exchangerate-api.com/v6/b81434a917841a6199be1cc7/latest/EUR");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    String palabras = stringBuilder.toString();
                    divisas = palabras.split(",");
                    for (int i = 0; i < 11; i++) {
                        divisas[i] = " ";
                    }
                    for (int i = 0; i < divisas.length; i++) {
                        System.out.print(divisas[i]);
                    }

                    bufferedReader.close();
                    inputStream.close();
                    connection.disconnect();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Cambio_Divisas.this, android.R.layout.simple_spinner_item, divisas);
                            spinner.setAdapter(adapter);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }).start();

        respuesta = findViewById(R.id.Respuesta);
        spinner = (Spinner)findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mostrar(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button BEuros = findViewById(R.id.Euro);
        BEuros.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Seleccionado.GlobalVariables.setValor(1);
                Seleccionado.GlobalVariables.setMyString("\"EUR\":1");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        respuesta.setText(Seleccionado.GlobalVariables.myString);
                    }
                });
            }
        });

    }


    public void mostrar(View view) {
        final String seleccionado = spinner.getSelectedItem().toString();
        if(seleccionado!=" ")
            Seleccionado.GlobalVariables.myString=seleccionado;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                respuesta.setText(Seleccionado.GlobalVariables.myString);
            }
        });

    }
}