package com.lksnext.arivas.view.fragment.reservas;

import static androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import java.util.TimeZone;
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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.android.material.chip.Chip;
import com.lksnext.arivas.R;
import com.lksnext.arivas.domain.CardAdapter;
import com.lksnext.arivas.domain.FutureDateValidator;
import com.lksnext.arivas.domain.PlazaAdapter;
import com.lksnext.arivas.view.fragment.MainFragment;
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

    private  String selectedChipType;


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

        ChipGroup chipGroup = rootView.findViewById(R.id.chipGroup);

        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (checkedIds.isEmpty()) {
                    Chip lastCheckedChip = group.findViewById(R.id.STD); // Default chip ID
                    lastCheckedChip.setChecked(true);
                    updateRecyclerView(rootView, "STD");
                } else {
                    int checkedId = checkedIds.get(0);
                    Chip selectedChip = group.findViewById(checkedId);
                    selectedChipType = selectedChip.getText().toString();
                    updateAdapterWithSelectedChipType();
                    updateRecyclerView(rootView, selectedChipType);
                }
            }
        });
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

        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener el NavController
        navController = Navigation.findNavController(view);

        // Configurar OnClickListener para la imagen de volver
    }

    public void createDatePicker(EditText etDate) {
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(new FutureDateValidator());
        Calendar futureMonth = Calendar.getInstance();

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Selecciona la fecha");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(selection);

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                String selectedDate = day + "/" + month + "/" + year;

                Toast.makeText(getContext(), "Fecha seleccionada: " + selectedDate, Toast.LENGTH_LONG).show();
                etDate.setText(selectedDate);
            }
        });

        materialDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
    }


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
                Toast.makeText(RealizarReservaFragment.this.getContext(), "Hora seleccionada: " + hourOfDay + ":" + minute , Toast.LENGTH_LONG).show();

            }
        });

        timePicker.show(getActivity().getSupportFragmentManager(), "timePicker");
    }
    private void updateAdapterWithSelectedChipType() {
        if (adapter != null) {
            adapter.setChipType(selectedChipType);
            adapter.notifyDataSetChanged();
        }
    }

    public  void updateRecyclerView(View rootView, String selectedChipType){
        recyclerView = rootView.findViewById(R.id.recyclerViewCards);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        dataSet = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) {
            dataSet.add(i);
        }

        adapter = new PlazaAdapter(dataSet, getContext(), selectedChipType);
        recyclerView.setAdapter(adapter);
    }

}
