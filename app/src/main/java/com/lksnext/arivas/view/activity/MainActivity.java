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

        // Asignamos la vista/interfaz main (layout)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Con el NavigationHost podremos movernos por distintas pestañas dentro de la misma pantalla
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.flFragment);
        navController = navHostFragment.getNavController();

        // Asignamos los botones de navegacion que se encuentran en la vista (layout)
        bottomNavigationView = binding.bottomNavigationView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Dependendiendo que boton clique el usuario de la navegacion se hacen distintas cosas
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.inicio) {
                navController.navigate(R.id.mainFragment);
                return true;
            } else if (itemId == R.id.reservas) {
                navController.navigate(R.id.reservasFragment);
                return true;
            } else if (itemId == R.id.reservasPasadas) {
                navController.navigate(R.id.reservasPasadasFragment);
                return true;
            }
            return false;
        });

        // Verificar si las plazas de estacionamiento ya están generadas
        checkAndPopulateParkingSlots();
    }

    private void checkAndPopulateParkingSlots() {
        db.collection("parking_slots").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        // No hay documentos en la colección, poblamos las plazas de estacionamiento
                        populateParkingSlots();
                    } else {
                        // Ya existen documentos en la colección
                        Log.d("MainActivity", "Las plazas de estacionamiento ya están generadas.");
                    }
                } else {
                    Log.w("MainActivity", "Error al obtener los documentos.", task.getException());
                }
            }
        });
    }

    private void populateParkingSlots() {
        // Define the data for each parking slot
        int slotId = 0;

        // STD slots
        for (int i = 0; i < 50; i++) {
            Map<String, Object> slot = new HashMap<>();
            slot.put("id", slotId++);
            slot.put("number", "STD" + i);
            slot.put("available", true);
            slot.put("type", "STD");
            db.collection("parking_slots").document("STD" + i)
                    .set(slot);
        }

        // ELEC slots
        for (int i = 0; i < 10; i++) {
            Map<String, Object> slot = new HashMap<>();
            slot.put("id", slotId++);
            slot.put("number", "ELEC" + i);
            slot.put("available", true);
            slot.put("type", "ELEC");
            db.collection("parking_slots").document("ELEC" + i)
                    .set(slot);
        }

        // MOTO slots
        for (int i = 0; i < 30; i++) {
            Map<String, Object> slot = new HashMap<>();
            slot.put("id", slotId++);
            slot.put("number", "MOTO" + i);  // Añadir número para consistencia
            slot.put("available", true);
            slot.put("type", "MOTO");
            db.collection("parking_slots").document("MOTO" + i)
                    .set(slot);
        }

        // DISC slots
        for (int i = 0; i < 10; i++) {
            Map<String, Object> slot = new HashMap<>();
            slot.put("id", slotId++);
            slot.put("number", "DISC" + i);
            slot.put("available", true);
            slot.put("type", "DISC");
            db.collection("parking_slots").document("DISC" + i)
                    .set(slot);
        }
    }
}