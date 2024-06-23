package com.lksnext.arivas.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.lksnext.arivas.R;
import com.lksnext.arivas.domain.CardAdapter;
import com.lksnext.arivas.view.activity.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String locationName = "Zuatzu Kalea, 3, 20018 Donostia, Gipuzkoa";
    private Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(locationName));



    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Button to navigate to SettingsActivity
        MaterialButton settingButton = rootView.findViewById(R.id.btnGoToSettings);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Button to navigate to another fragment (assuming you have a navigation graph)
        MaterialButton realizarReservaButton = rootView.findViewById(R.id.btnRealizarReserva);
        realizarReservaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.realizarReservaFragment);
            }
        });

        // Button to open Google Maps
        MaterialButton comoLlegarButton = rootView.findViewById(R.id.comollegar);
        comoLlegarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check and request location permission if needed
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    obtenerUbicacionYAbrirGoogleMaps();
                }
            }
        });

        // RecyclerView setup
        recyclerView = rootView.findViewById(R.id.recyclerViewCards);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Dummy data for RecyclerView
        List<Integer> dataSet = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            dataSet.add(i);
        }

        // Adapter setup
        adapter = new CardAdapter(getParentFragmentManager(), dataSet);
    recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get location and open Google Maps
                obtenerUbicacionYAbrirGoogleMaps();
            } else {
                // Permission denied, handle this as needed (e.g., show a message)
            }
        }
    }

    private void obtenerUbicacionYAbrirGoogleMaps() {
        // Replace with logic to obtain actual device location
        // For demo purposes, opening Google Maps with a sample location (Googleplex)
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
