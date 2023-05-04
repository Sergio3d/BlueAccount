package com.cumn.blueaccount.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.cumn.blueaccount.MainActivity;
import com.cumn.blueaccount.R;
import com.cumn.blueaccount.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView cantTotal, nombreGrupo;
    private static float cantidad;
    private ArrayList<Object> libroCuentas;
    private RecyclerView lista;
    //final TransacListAdapter adapter = new TransacListAdapter(this);

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lista = (RecyclerView) root.findViewById(R.id.Lista);
        cantTotal = root.findViewById(R.id.cantTotal);
        nombreGrupo = root.findViewById(R.id.textNombreGrupo);
        libroCuentas = new ArrayList<Object>();


        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.rutaPreferences),Context.MODE_PRIVATE);
        String grupo = sharedPref.getString("grupoActual", "Yo");

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blueaccount-e4707-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference misCuentas = database.getReference("/Grupos/"+ grupo + "/Cuentas");

        nombreGrupo.setText(grupo);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cantidad = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child("Mov").getValue() != null){
                        libroCuentas.add(child.child("Mov").getValue());
                        cantidad = cantidad + Float.parseFloat(Objects.requireNonNull(child.child("Mov").getValue()).toString());
                    }
                }
                cantTotal.setText(String.valueOf(cantidad));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        misCuentas.addListenerForSingleValueEvent(eventListener);

        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public float getCantidad() {
        return cantidad;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}