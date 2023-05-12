package com.cumn.blueaccount.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.cumn.blueaccount.CambiarGrupoActivity;
import com.cumn.blueaccount.CreaGrupoActivity;
import com.cumn.blueaccount.MainActivity;
import com.cumn.blueaccount.R;
import com.cumn.blueaccount.databinding.FragmentGruposBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private FragmentGruposBinding binding;
    private int nGrupos;
    private Button creaGrupo, cambiaGrupo, salirGrupo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        NotificationsViewModel notificationsViewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(NotificationsViewModel.class);

        binding = FragmentGruposBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        creaGrupo = root.findViewById(R.id.bCreaGrupo);
        creaGrupo.setOnClickListener(v -> {
            Intent creagrupo = new Intent(NotificationsFragment.this.getContext(), CreaGrupoActivity.class);
            startActivity(creagrupo);
        });

        cambiaGrupo = root.findViewById(R.id.bCambiaGrupo);
        cambiaGrupo.setOnClickListener(v -> {
            Intent cambiargrupo = new Intent(NotificationsFragment.this.getContext(), CambiarGrupoActivity.class);
            startActivity(cambiargrupo);
        });

        salirGrupo = root.findViewById(R.id.bSalirGrupo);
        salirGrupo.setOnClickListener(v -> {
            nGrupos = 0;
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String user = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://blueaccount-e4707-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference myRef = database.getReference("/Grupos/");
            myRef.get().addOnCompleteListener((OnCompleteListener<DataSnapshot>) task -> {
                if (task.isSuccessful()) {
                    for (DataSnapshot child : task.getResult().getChildren()) {
                        for (DataSnapshot member : child.child("Usuarios").getChildren()){
                            if(Objects.requireNonNull(member.getValue()).toString().equals(user)){
                                nGrupos++;
                            }
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(NotificationsFragment.this.getContext(), "Error", Toast.LENGTH_LONG);
                    toast.show();
                }
                String grupo = MainActivity.getGrupoActual();
                DatabaseReference usersRef = database.getReference("/Grupos/" + grupo + "/Usuarios/");
                usersRef.get().addOnCompleteListener(task2 -> {
                    if (task2.getResult().getChildrenCount() <= 1) {
                        if(nGrupos>1) {
                            Objects.requireNonNull(usersRef.getParent()).removeValue();
                            Intent cambiargrupo = new Intent(NotificationsFragment.this.getContext(), CambiarGrupoActivity.class);
                            startActivity(cambiargrupo);
                        }else{
                            Toast toast = Toast.makeText(NotificationsFragment.this.getContext(), "Tienes que estar en al menos un grupo", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else {
                        DatabaseReference delUser;
                        for (DataSnapshot child : task2.getResult().getChildren()) {
                            if (child.getValue().equals(user)) {
                                delUser = child.getRef();
                                delUser.removeValue();
                                Intent cambiargrupo = new Intent(NotificationsFragment.this.getContext(), CambiarGrupoActivity.class);
                                startActivity(cambiargrupo);
                            }
                        }
                    }
                });
            });

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}