package com.lksnext.arivas.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lksnext.arivas.R;
import com.lksnext.arivas.adapters.CardAdapter;
import com.lksnext.arivas.domain.Reservation;
import com.lksnext.arivas.view.activity.SettingsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private Boolean areThereReservations;



    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();

        MaterialButton settingButton = rootView.findViewById(R.id.btnGoToSettings);
        settingButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        MaterialButton realizarReservaButton = rootView.findViewById(R.id.btnRealizarReserva);
        realizarReservaButton.setOnClickListener(v -> {
            navController.navigate(R.id.realizarReservaFragment);
        });

        MaterialButton comoLlegarButton = rootView.findViewById(R.id.comollegar);
        comoLlegarButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                obtenerUbicacionYAbrirGoogleMaps();
            }
        });

        CircularProgressIndicator progressIndicatorSTD = rootView.findViewById(R.id.progress_STD);
        CircularProgressIndicator progressIndicatorMOTO = rootView.findViewById(R.id.progress_MOTO);
        CircularProgressIndicator progressIndicatorELEC = rootView.findViewById(R.id.progress_ELEC);
        CircularProgressIndicator progressIndicatorDISC = rootView.findViewById(R.id.progress_DISC);

        TextView tvFreeSTD = rootView.findViewById(R.id.free_places_auto_tv);
        TextView tvFreeMOTO = rootView.findViewById(R.id.free_places_moto_tv);
        TextView tvFreeELEC = rootView.findViewById(R.id.free_places_elect_tv);
        TextView tvFreeDISC = rootView.findViewById(R.id.free_places_disc_tv);

        progressIndicatorSTD.setProgress(getProgress(getNumOfTypeVehicles("STD"), 50) ,true);
        progressIndicatorMOTO.setProgress(getProgress(getNumOfTypeVehicles("MOTO"), 30) ,true);
        progressIndicatorELEC.setProgress(getProgress(getNumOfTypeVehicles("ELEC"), 10) ,true);
        progressIndicatorDISC.setProgress(getProgress(getNumOfTypeVehicles("DISC"), 10) ,true);

        tvFreeSTD.setText(getFreeSlots(getNumOfTypeVehicles("STD"), 50)+ "/50");
        tvFreeMOTO.setText(getFreeSlots(getNumOfTypeVehicles("MOTO"), 30)+ "/30");
        tvFreeELEC.setText(getFreeSlots(getNumOfTypeVehicles("ELEC"), 10)+ "/10");
        tvFreeDISC.setText(getFreeSlots(getNumOfTypeVehicles("DISC"), 10)+ "/10");

        loadReservationsAndUpdateUI(rootView, userUUID);

        return rootView;
    }

    private void loadReservationsAndUpdateUI(View rootView, String userUUID) {
        firestore.collection("reservations")
                .whereEqualTo("uid", userUUID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean hasReservations = !task.getResult().isEmpty();
                        if (hasReservations) {
                            rootView.findViewById(R.id.no_reservations).setVisibility(View.GONE);
                            initializeRecyclerView(rootView, userUUID);
                        } else {
                            rootView.findViewById(R.id.no_reservations).setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error al obtener las reservas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initializeRecyclerView(View rootView, String userUUID) {
        recyclerView = rootView.findViewById(R.id.recyclerViewCards);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        userReservations = new ArrayList<>();
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
                        Collections.sort(userReservations, Comparator.comparing(Reservation::getDate));
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireContext(), "Error al obtener las reservas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void updateRecycler(View rootView, String userUUID) {
        System.out.println(areThereReservations);
        if (areThereReservations) {
            rootView.findViewById(R.id.no_reservations).setVisibility(View.GONE);
            recyclerView = rootView.findViewById(R.id.recyclerViewCards);
            recyclerView.setHasFixedSize(true);

            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);

            userReservations = new ArrayList<>();
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
                            Collections.sort(userReservations, (r1, r2) -> r1.getDate().compareTo(r2.getDate()));
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(requireContext(), "Error al obtener las reservas", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
        long fixedHourMillis = getYesterdayAtFixedHourMillis();

        return reservationTimeMillis > fixedHourMillis;
    }

    public int getFreeSlots(int used, int total){
        return total-used;
    }
    public int getProgress(int used, int total) {
        if (total == 0) {
            return 0;
        }
        double progress = (1-(double) used / total) * 100;
        return (int) progress;
    }
        public int getNumOfTypeVehicles(String vehicleType) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currentDateStr = dateFormat.format(new Date());

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String currentTimeStr = timeFormat.format(new Date());

            TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();

            firestore.collection("reservations")
                    .whereEqualTo("type", vehicleType)
                    .whereEqualTo("date", currentDateStr)
                    .whereEqualTo("in", currentTimeStr)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    count++;
                                }
                                taskCompletionSource.setResult(count);
                            } else {
                                taskCompletionSource.setResult(0);
                            }
                        }
                    });

            try {
                return taskCompletionSource.getTask().getResult();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    public boolean areThereReservations(String userUUID) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        firestore.collection("reservations")
                .whereEqualTo("uid", userUUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            taskCompletionSource.setResult(!task.getResult().isEmpty());
                        }
                    }
                });

        try {
            return taskCompletionSource.getTask().getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
