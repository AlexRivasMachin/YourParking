package com.lksnext.arivas.view.fragment.reservas;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lksnext.arivas.R;
import com.lksnext.arivas.domain.CardAdapter;
import com.lksnext.arivas.domain.PastReservationAdapter;
import com.lksnext.arivas.domain.Reservation;
import com.lksnext.arivas.viewmodel.reservas.ReservasPasadasViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ReservasPasadasFragment extends Fragment {

    private ReservasPasadasViewModel mViewModel;
    private NavController navController;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore firestore;
    private List<Reservation> userReservations;
    private MaterialButtonToggleGroup toggleGroup;
    List<String> selectedTypes = new ArrayList<>();

    public static ReservasPasadasFragment newInstance() {
        return new ReservasPasadasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservas_pasadas, container, false);

        toggleGroup = rootView.findViewById(R.id.toggleButton);
        toggleGroup.setSingleSelection(false);

        MaterialButton realizarReservaButton = rootView.findViewById(R.id.realizarReservaFab);
        realizarReservaButton.setOnClickListener(v -> {
            if (navController != null) {
                navController.navigate(R.id.realizarReservaFragment);
            } else {
                Log.e("ReservasPasadasFragment", "NavController is null");
            }
        });
        String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestore = FirebaseFirestore.getInstance();

        updateRecycler(rootView, userUUID, selectedTypes);

        setupToggleListener(rootView);

        return rootView;
    }

    private void setupToggleListener(View rootView) {
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            selectedTypes.clear();

            for (int i = 0; i < toggleGroup.getChildCount(); i++) {
                View view = toggleGroup.getChildAt(i);
                if (view instanceof MaterialButton && ((MaterialButton) view).isChecked()) {
                    selectedTypes.add(getResources().getResourceEntryName(view.getId()));
                }
            }
            updateRecycler(rootView, FirebaseAuth.getInstance().getCurrentUser().getUid(), selectedTypes);
        });
    }

    private void updateRecycler(View rootView, String userUUID, List<String> types) {
        recyclerView = rootView.findViewById(R.id.recyclerViewPastReservations);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        userReservations = new ArrayList<>();
        adapter = new PastReservationAdapter(userReservations, getContext());
        recyclerView.setAdapter(adapter);

        fetchReservations(userUUID, types);
    }

    private void fetchReservations(String userUUID, List<String> types) {
        firestore.collection("reservations")
                .whereEqualTo("uid", userUUID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userReservations.clear();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Reservation reservation = documentSnapshot.toObject(Reservation.class);
                            if (isPastReservation(reservation.getDate()) && types.contains(reservation.getType())) {
                                userReservations.add(reservation);
                                System.out.println("Reserva: " + reservation);
                            } else if (types.isEmpty() && isPastReservation(reservation.getDate())) {
                                userReservations.add(reservation);
                            }
                        }
                        Collections.sort(userReservations, (r1, r2) -> r1.getDate().compareTo(r2.getDate()));
                        adapter.notifyDataSetChanged();
                        CheckIfEmpty();
                    } else {

                    }
                });
    }
    public void CheckIfEmpty() {
        if (userReservations.isEmpty()) {
            getView().findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.textView2).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.imageView7).setVisibility(View.GONE);
            getView().findViewById(R.id.textView2).setVisibility(View.GONE);
        }
    }
    public long stringToMilis(String dateString) {
        return getTimeWithFixedHour(stringToDate(dateString), 20, 0);
    }

    public  Date stringToDate(String dateString) {
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

    private boolean isPastReservation(String dateString) {
        long reservationTimeMillis = stringToMilis(dateString);
        long fixedHourMillis = getYesterdayAtFixedHourMillis();
        return reservationTimeMillis < fixedHourMillis;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReservasPasadasViewModel.class);
    }

}