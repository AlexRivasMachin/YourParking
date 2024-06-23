package com.lksnext.arivas.view.fragment.reservas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.lksnext.arivas.R;
import com.lksnext.arivas.domain.CardAdapter;
import com.lksnext.arivas.domain.PlazaAdapter;
import com.lksnext.arivas.viewmodel.reservas.ReservasViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RealizarReservaFragment extends Fragment {

    private NavController navController;
    private ReservasViewModel mViewModel;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private PlazaAdapter adapter;
    private List<Integer> dataSet;

    public static RealizarReservaFragment newInstance() {
        return new RealizarReservaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_realizar_reserva, container, false);
        Button continuarReservaButton = rootView.findViewById(R.id.btnContinuar);
        continuarReservaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = NavHostFragment.findNavController(RealizarReservaFragment.this);
                    navController.navigate(R.id.confirmarReservaFragment);
                }
            }
        );
        EditText etDate = rootView.findViewById(R.id.et_date);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePicker(etDate);
            }
        });

        EditText etTimeEntry = rootView.findViewById(R.id.et_time_entry);
        etTimeEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePicker(etTimeEntry);
            }
        });

        EditText etTimeExit = rootView.findViewById(R.id.et_time_exit);
        etTimeExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePicker(etTimeExit);
            }
        });

        // Configurar RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerViewCards);
        recyclerView.setHasFixedSize(true);

        // Usar un GridLayoutManager para la disposición de las tarjetas
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Crear dataset
        dataSet = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) {
            dataSet.add(i);
        }

        // Crear y asignar el adaptador
        adapter = new PlazaAdapter(dataSet, getContext());
        recyclerView.setAdapter(adapter);

        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener el NavController
        navController = Navigation.findNavController(view);

        // Configurar OnClickListener para la imagen de volver
        view.findViewById(R.id.volverImageRealizarReserva1).setOnClickListener(v -> navController.popBackStack(R.id.reservasFragment, false));
    }

    public void createDatePicker(EditText etDate) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Selecciona la fecha");
        MaterialDatePicker<Long> materialDatePicker = builder.build();

        materialDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");

    };

    public void createTimePicker(EditText etTime) {
        int hour = 8;
        int minute = 0;

        MaterialTimePicker.Builder timePickerBuilder = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .setTitleText("Select Time");

        MaterialTimePicker timePicker = timePickerBuilder.build();

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourOfDay = timePicker.getHour();
                int minute = timePicker.getMinute();
                etTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }
        });

        timePicker.show(getActivity().getSupportFragmentManager(), "timePicker");
    }


}
