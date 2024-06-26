package com.lksnext.arivas.view.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.lksnext.arivas.R;
import com.lksnext.arivas.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BottomNavigationView bottomNavigationView;
    ActivityMainBinding binding;
    NavController navController;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.flFragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        bottomNavigationView = binding.bottomNavigationView;
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mainFragment, R.id.realizarReservaFragment, R.id.reservasPasadasFragment)
                .build();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.inicio:
                    navController.navigate(R.id.mainFragment);
                    return true;
                case R.id.reservasPasadas:
                    navController.navigate(R.id.reservasPasadasFragment);
                    return true;
                case R.id.realizarReserva:
                    navController.navigate(R.id.realizarReservaFragment);
                    return true;
                default:
                    return false;
            }
        });

        checkAndPopulateParkingSlots();
    }


    private void checkAndPopulateParkingSlots() {
        db.collection("parking_slots").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        populateParkingSlots();
                    } else {
                        Log.d("MainActivity", "Las plazas de estacionamiento ya están generadas.");
                    }
                } else {
                    Log.w("MainActivity", "Error al obtener los documentos.", task.getException());
                }
            }
        });
    }
    private void populateParkingSlots() {
        populateParkingType(50, "STD");
        populateParkingType(10, "ELEC");
        populateParkingType(30, "MOTO");
        populateParkingType(10, "DISC");
    }
    private void populateParkingType(int slots, String type) {
        for (int i = 0; i < slots; i++) {
            Map<String, Object> slot = new HashMap<>();
            slot.put("id", i);
            slot.put("number", type + i);
            slot.put("available", true);
            slot.put("type", type);
            db.collection("parking_slots").document(type + i)
                    .set(slot);
        }

    }
}