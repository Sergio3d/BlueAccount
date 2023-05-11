package com.cumn.blueaccount.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cumn.blueaccount.MainActivity;
import com.cumn.blueaccount.R;
import com.cumn.blueaccount.Seleccionado;
import com.cumn.blueaccount.databinding.FragmentHomeBinding;
import com.cumn.blueaccount.models.TransacAdapter;
import com.cumn.blueaccount.models.TransacEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lista = (RecyclerView) root.findViewById(R.id.Lista);
        cantTotal = root.findViewById(R.id.cantTotal);
        nombreGrupo = root.findViewById(R.id.textNombreGrupo);
        libroCuentas = new ArrayList<Object>();

        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //nombreGrupo.setText(nameGrupo);

        /*SharedPreferences sharedPref = HomeFragment.this.getParentFragment().getActivity().getSharedPreferences(getString(R.string.rutaPreferences),Context.MODE_PRIVATE);
        String grupo = sharedPref.getString("grupoActual", "Yo");*/
        String grupo = MainActivity.getGrupoActual();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blueaccount-e4707-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference misCuentas = database.getReference("/Grupos/"+ grupo );


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cantidad = 0;
                ArrayList<TransacEntity> listTransac = new ArrayList<TransacEntity>();
                for (DataSnapshot child : snapshot.child("Cuentas").getChildren()) {
                    if (child.getValue() != null){
                        libroCuentas.add(child.child("Mov").getValue());
                        cantidad = cantidad + Float.parseFloat(Objects.requireNonNull(child.child("Mov").getValue()).toString());
                        TransacEntity newTransac = new TransacEntity(child.getKey() ,
                                Objects.requireNonNull(child.child("Desc").getValue()).toString(),
                                Objects.requireNonNull(child.child("Mov").getValue()).toString(),
                                Objects.requireNonNull(child.child("Fecha").getValue()).toString(),
                                Objects.requireNonNull(child.child("User").getValue()).toString());
                        listTransac.add(0,newTransac);
                    }
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(HomeFragment.this.getContext());
                TransacAdapter adapter = new TransacAdapter(listTransac);
                lista.setLayoutManager(layoutManager);
                lista.setAdapter(adapter);
                cantTotal.setText(String.valueOf(cantidad * Seleccionado.GlobalVariables.getValor()));
                nombreGrupo.setText(snapshot.child("NombreGrupo").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        misCuentas.addValueEventListener(eventListener);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}