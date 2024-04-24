package com.lksnext.arivas.view.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.lksnext.arivas.R;



public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Obtén el NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        // Asegúrate de que el NavHostFragment no sea null
        if (navHostFragment != null) {
            // Obtén el NavController
            NavController navController = navHostFragment.getNavController();

            // Configura la navegación
            // NavigationUI.setupActionBarWithNavController(this, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView)).getNavController();
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}

