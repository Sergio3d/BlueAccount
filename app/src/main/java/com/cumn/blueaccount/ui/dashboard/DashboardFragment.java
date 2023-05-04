package com.cumn.blueaccount.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.cumn.blueaccount.MainActivity;
import com.cumn.blueaccount.R;
import com.cumn.blueaccount.databinding.FragmentNuevoBinding;
import com.cumn.blueaccount.ui.home.HomeFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private FragmentNuevoBinding binding;
    private RadioButton GastoButton, IngresoButton;
    private Button createButton;
    private CalendarView inputCalendar;
    private EditText inputCantidad, inputFecha, inputDescripcion;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel.class);

        binding = FragmentNuevoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.i("BAcc", "Se crea la vista Nuevo");

        GastoButton  = root.findViewById(R.id.GastoButton);
        IngresoButton = root.findViewById(R.id.IngresoButton);
        inputCantidad = root.findViewById(R.id.inputCantidad);
        inputCalendar = root.findViewById(R.id.calendarView);
        inputDescripcion = root.findViewById(R.id.inputDescripcion);





        createButton = root.findViewById(R.id.createButton);
        createButton.setOnClickListener(v -> {

            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.rutaPreferences),Context.MODE_PRIVATE);
            String grupo = sharedPref.getString("grupoActual", "Yo");
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://blueaccount-e4707-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference myRef = database.getReference("/Grupos/"+ grupo + "/Cuentas");

            float cantidad;
            if ((!TextUtils.isEmpty(GastoButton.getText()) || !TextUtils.isEmpty(IngresoButton.getText())) && !TextUtils.isEmpty(inputCantidad.getText())) {
                if (GastoButton.isChecked()) {
                    cantidad = 0 - Float.parseFloat(inputCantidad.getText().toString().replace(",", "."));
                } else {
                    cantidad = Float.parseFloat(inputCantidad.getText().toString().replace(",", "."));
                }

                String fechaConvertida = null;

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                // Write a message to the database
                DatabaseReference cuenta = myRef.push();

                cuenta.child("Mov").setValue(cantidad);
                cuenta.child("Desc").setValue(inputDescripcion.getText().toString());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(inputCalendar.getDate());
                fechaConvertida = dateFormat.format(calendar.getTime());
                cuenta.child("Fecha").setValue(fechaConvertida);

                Toast toast = Toast.makeText(this.getContext(), "Cuenta Nueva", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}