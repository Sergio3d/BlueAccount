package com.cumn.blueaccount.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cumn.blueaccount.CreaGrupoActivity;
import com.cumn.blueaccount.MainActivity;
import com.cumn.blueaccount.R;
import com.cumn.blueaccount.databinding.FragmentGruposBinding;

import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private FragmentGruposBinding binding;

    private Button creaGrupo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NotificationsViewModel.class);

        binding = FragmentGruposBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        creaGrupo = root.findViewById(R.id.bCreaGrupo);
        creaGrupo.setOnClickListener(v -> {
            Intent creagrupo = new Intent(NotificationsFragment.this.getContext(), CreaGrupoActivity.class);
            startActivity(creagrupo);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}