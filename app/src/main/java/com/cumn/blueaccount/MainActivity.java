package com.cumn.blueaccount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cumn.blueaccount.databinding.ActivityMainBinding;

import java.util.Arrays;

import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String Api_Base_URL = "https://api.exchangerate.host/latest";

    private ActivityMainBinding binding;
    final static String LOG_TAG = "btb";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static String grupoActual;

    public static String getGrupoActual() {
        return grupoActual;
    }

    public static void setGrupoActual(String grupoActual) {
        MainActivity.grupoActual = grupoActual;
    }


    private static final int RC_SIGN_IN = 2022;
    private ExangeRate exangeRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Grupo
        this.setGrupoActual("Viaje Londres");

        //AUTH

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    CharSequence username = user.getDisplayName();
                    Toast.makeText(MainActivity.this, getString(R.string.firebase_user_fmt, username), Toast.LENGTH_LONG).show();
                    Log.i(LOG_TAG, "onAuthStateChanged() " + getString(R.string.firebase_user_fmt, username));

                } else {
                    // user is signed out
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app
                            AuthUI.getInstance().
                                    createSignInIntentBuilder().
                                    setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    )).
                                    setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                                    //setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                                            build(),
                            RC_SIGN_IN
                    );

                    //Remains logged in when unique gmail account
                    // setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                    //setIsSmartLockEnabled(BuildConfig.DEBUG /* credentials */, true /* hints */).

                }
            }
        };

        setContentView(binding.getRoot());
    }

    protected void onPause() {
            super.onPause();
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

        @Override
        protected void onResume() {
            super.onResume();
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, R.string.signed_in, Toast.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_in));
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, R.string.signed_cancelled, Toast.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_cancelled));
                    finish();
                }
            }


        //de la api
        exangeRate = ExangeRateClient.getClient().create(ExangeRate.class);

        Call<ExangerateResponse> call = exangeRate.getLatestExangeRates("EUR","USD");
        call.enqueue(new Callback<ExangerateResponse>() {
            @Override
            public void onResponse(Call<ExangerateResponse> call, Response<ExangerateResponse> response) {
                if (response.isSuccessful()) {
                    ExangerateResponse exangerateResponse = response.body();
                    String baseCurrency = exangerateResponse.getBase();
                    String date = exangerateResponse.getDate();
                    ExangeRate rates = exangerateResponse.getRates();
                } else {
                    int error = response.code();
                    String errorMessage = response.message();
                }
            }

            @Override
            public void onFailure(Call<ExangerateResponse> call, Throwable t) {

            }
        });
    }
}
