package com.cumn.blueaccount.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cumn.blueaccount.R;
import com.cumn.blueaccount.databinding.FragmentNuevoBinding;
import com.cumn.blueaccount.ui.home.HomeFragment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DashboardFragment extends Fragment {

    private FragmentNuevoBinding binding;
    private RadioButton GastoButton, IngresoButton;
    private Button createButton;
    private EditText inputCantidad, inputFecha, inputDescripcion;
    private AutoCompleteTextView inputEtiqueta;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel.class);

        binding = FragmentNuevoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.i("BAcc", "Se crea la vista Nuevo");

        GastoButton  = root.findViewById(R.id.GastoButton);
        IngresoButton = root.findViewById(R.id.IngresoButton);
        createButton = root.findViewById(R.id.createButton);
        inputCantidad = root.findViewById(R.id.inputCantidad);
        inputFecha = root.findViewById(R.id.inputFecha);
        inputDescripcion = root.findViewById(R.id.inputDescripcion);
        inputEtiqueta = root.findViewById(R.id.inputEtiqueta);

        createButton = (Button) root.findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> {

            HomeFragment.sumarCantidad(10);
            Context context = this.getContext();
            CharSequence text = "+10€";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });


        //final TextView textView = binding.textNuevo;
        // dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}