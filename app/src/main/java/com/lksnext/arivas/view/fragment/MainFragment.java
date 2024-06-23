package com.lksnext.arivas.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lksnext.arivas.R;
import com.lksnext.arivas.domain.CardAdapter;
import com.lksnext.arivas.view.activity.SettingsActivity;
import com.lksnext.arivas.view.fragment.reservas.ReservasFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public MainFragment() {
        // Es necesario un constructor vacio
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Asignar la vista (layout) al fragmento
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button settingButton = rootView.findViewById(R.id.btnGoToSettings);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Obtén una referencia al botón en tu diseño
        Button realizarReservaButton = rootView.findViewById(R.id.btnRealizarReserva);

        // Configura un OnClickListener para el botón
        realizarReservaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega al fragmento fragmentRealizarReserva
                NavController navController = NavHostFragment.findNavController(MainFragment.this);
                navController.navigate(R.id.realizarReservaFragment);
            }
        });

        // Configurar RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerViewCards);
        recyclerView.setHasFixedSize(true);

        // Usar un GridLayoutManager para la disposición de las tarjetas
        layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Crear dataset
        List<Integer> dataSet = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            dataSet.add(i);
        }

        // Crear y asignar el adaptador
        adapter = new CardAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        navController.popBackStack(R.id.mainFragment, false);
    }
}
