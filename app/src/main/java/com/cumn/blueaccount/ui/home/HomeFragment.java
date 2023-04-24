package com.cumn.blueaccount.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cumn.blueaccount.R;
import com.cumn.blueaccount.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView cantTotal;
    private static float cantidad;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.i("BAcc", "Se crea la vista Home");
        //final RecyclerView lista = (RecyclerView) findViewById(R.id.Lista);
        //final TransacListAdapter adapter = new TransacListAdapter(this);

        cantTotal = root.findViewById(R.id.cantTotal);
        cantTotal.setText(String.valueOf(cantidad));
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public float getCantidad() {
        return cantidad;
    }

    public static void sumarCantidad(float cantidad) {
        HomeFragment.cantidad = HomeFragment.cantidad + cantidad;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}