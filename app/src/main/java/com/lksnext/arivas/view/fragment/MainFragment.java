package com.lksnext.arivas.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lksnext.arivas.R;
import com.lksnext.arivas.domain.CardAdapter;
import com.lksnext.arivas.domain.PlazaAdapter;
import com.lksnext.arivas.domain.Reservation;
import com.lksnext.arivas.view.activity.SettingsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String locationName = "Zuatzu Kalea, 3, 20018 Donostia, Gipuzkoa";
    private Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(locationName));
    private List<Reservation> userReservations;
    private FirebaseFirestore firestore;



    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        MaterialButton settingButton = rootView.findViewById(R.id.btnGoToSettings);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        MaterialButton realizarReservaButton = rootView.findViewById(R.id.btnRealizarReserva);
        realizarReservaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.realizarReservaFragment);
            }
        });

        MaterialButton comoLlegarButton = rootView.findViewById(R.id.comollegar);
        comoLlegarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestore = FirebaseFirestore.getInstance();

        updateRecycler(rootView, userUUID);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void updateRecycler(View rootView, String userUUID) {
        recyclerView = rootView.findViewById(R.id.recyclerViewCards);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        userReservations = new ArrayList<Reservation>();
        adapter = new CardAdapter(requireFragmentManager(), userReservations);
        recyclerView.setAdapter(adapter);

        firestore.collection("reservations")
                .whereEqualTo("uid", userUUID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Reservation reservation = documentSnapshot.toObject(Reservation.class);
                            if (isNotPastReservation(reservation.getDate())) {
                                userReservations.add(reservation);
                            }
                        }
                        Collections.sort(userReservations, new Comparator<Reservation>() {
                            @Override
                            public int compare(Reservation r1, Reservation r2) {
                                return r1.getDate().compareTo(r2.getDate());
                            }
                        });

                        adapter.notifyDataSetChanged();
                    }
                });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionYAbrirGoogleMaps();
            }
        }
    }

    private void obtenerUbicacionYAbrirGoogleMaps() {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(requireContext(), "Google Maps no está instalado en el dispositivo", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error al abrir Google Maps: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public long stringToMilis(String dateString) {
        return getTimeWithFixedHour(stringToDate(dateString), 20, 0);
    }

    public Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public long getTimeWithFixedHour(Date date, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
    public long getYesterdayAtFixedHourMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return getTimeWithFixedHour(calendar.getTime(), 20, 0);
    }

    private boolean isNotPastReservation(String dateString) {
        long reservationTimeMillis = stringToMilis(dateString);
        long fixedHourMillis = getYesterdayAtFixedHourMillis(); // Obtener milisegundos con hora 20:00 de ayer

        return reservationTimeMillis > fixedHourMillis;
    }

}
