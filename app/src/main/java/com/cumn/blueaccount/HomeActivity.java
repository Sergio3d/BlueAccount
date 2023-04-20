package com.cumn.blueaccount;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private TextView cantTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);


        Log.i("BAcc", "Se crea la vista Home");
        //final RecyclerView lista = (RecyclerView) findViewById(R.id.Lista);
        //final TransacListAdapter adapter = new TransacListAdapter(this);

        cantTotal = findViewById(R.id.cantTotal);
        cantTotal.setText(this.getTotal());
    }

    public static String getTotal(){

        Log.i("BAcc", "Inicia getTotal");
        float cantTotal=0;
        String line = "";
        final String delimiter = ";";
        try
        {
            String filePath = "librocuentas.csv";
            FileReader fileReader = new FileReader(filePath);
            if (fileReader!=null) {

                Log.i("BAcc", "El fichero existe");
                BufferedReader reader = new BufferedReader(fileReader);
                while ((line = reader.readLine()) != null)   //loops through every line until null found
                {
                    String[] token = line.split(delimiter);    // separate every token by comma
                    cantTotal = cantTotal + Float.parseFloat(token[1]);
                }
            }else{
                Log.i("BAcc", "El fichero no existe");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return String.valueOf(cantTotal);
    }
    /*
    private ListView listView;
    List<TransacEntity> rowItems;
    private ArrayAdapter<TransacEntity> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        rowItems = new ArrayList<TransacEntity>();
        for (int i = 0; i < names.length; i++) {
            TransacEntity item = new TransacEntity(names[i], images[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.Lista);
        RowArrayAdapter adapter = new RowArrayAdapter(this,
                R.layout.row_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this.onItemClick());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Item " + (position + 1) + ": " + rowItems.get(position),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
    */

}