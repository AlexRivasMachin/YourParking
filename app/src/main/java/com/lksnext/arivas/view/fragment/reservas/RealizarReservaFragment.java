package com.lksnext.arivas.view.fragment.reservas;

import static androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;
import java.util.TimeZone;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.ktx.Firebase;
import com.lksnext.arivas.R;
import com.lksnext.arivas.domain.CardAdapter;
import com.lksnext.arivas.domain.FutureDateValidator;
import com.lksnext.arivas.domain.PlazaAdapter;
import com.lksnext.arivas.domain.Reservation;
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
    private List<String> dataSet;
    private FirebaseFirestore firestore;
    private  String selectedChipType;
    private String selectedChip;



    public static RealizarReservaFragment newInstance() {
        return new RealizarReservaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_realizar_reserva, container, false);
        firestore = FirebaseFirestore.getInstance();

        ChipGroup chipGroup = rootView.findViewById(R.id.chipGroup);

        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if(checkedIds.size() == 0){
                    rootView.findViewById(R.id.STD);
                }else {
                    int chipId = group.getCheckedChipId();
                    Chip selectedChip = rootView.findViewById(chipId);
                    selectedChipType = getCHipType((String) selectedChip.getText());
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
                createTimePicker(etTimeEntry, null);
            }
        });
        EditText etTimeExit = rootView.findViewById(R.id.et_time_exit);
        etTimeExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePicker(etTimeExit, etTimeEntry);
            }
        });

        Button continuarReservaButton = rootView.findViewById(R.id.btnContinuar);
        continuarReservaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areAllFieldsCompleted(rootView)) {
                    createDialog(rootView);
                } else {
                    Toast.makeText(getContext(), "Por favor, complete todos los campos.", Toast.LENGTH_LONG).show();
                }
            }
        });

        updateRecyclerView(rootView, "STD");

        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

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


    public void createTimePicker(EditText etTime, @Nullable EditText etTimeEntry) {
        int hour = 8;
        int minute = 0;

        MaterialTimePicker.Builder timePickerBuilder = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .setTitleText("Selecciona la hora");

        MaterialTimePicker timePicker = timePickerBuilder.build();

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourOfDay = timePicker.getHour();
                int minute = timePicker.getMinute();
                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

                if (etTimeEntry != null) { // Estamos seleccionando la hora de salida
                    String entryTime = etTimeEntry.getText().toString();
                    if (!entryTime.isEmpty()) {
                        String[] parts = entryTime.split(":");
                        int entryHour = Integer.parseInt(parts[0]);
                        int entryMinute = Integer.parseInt(parts[1]);

                        Calendar calendarEntry = Calendar.getInstance();
                        calendarEntry.set(Calendar.HOUR_OF_DAY, entryHour);
                        calendarEntry.set(Calendar.MINUTE, entryMinute);

                        Calendar calendarExit = Calendar.getInstance();
                        calendarExit.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendarExit.set(Calendar.MINUTE, minute);

                        long difference = calendarExit.getTimeInMillis() - calendarEntry.getTimeInMillis();
                        if (difference > 8 * 60 * 60 * 1000) { // Más de 8 horas
                            calendarExit.setTimeInMillis(calendarEntry.getTimeInMillis() + 8 * 60 * 60 * 1000);
                            hourOfDay = calendarExit.get(Calendar.HOUR_OF_DAY);
                            minute = calendarExit.get(Calendar.MINUTE);
                            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        }
                    }
                }

                etTime.setText(selectedTime);
                Toast.makeText(RealizarReservaFragment.this.getContext(), "Hora seleccionada: " + selectedTime, Toast.LENGTH_SHORT).show();
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
        recyclerView = rootView.findViewById(R.id.recyclerViewChips);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        dataSet = new ArrayList<String>();
        adapter = new PlazaAdapter(dataSet, getContext(), selectedChipType);
        recyclerView.setAdapter(adapter);

        firestore.collection("parking_slots")
                .whereEqualTo("type", selectedChipType)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String numeroPlaza = id.replaceAll("[^0-9]","");
                            dataSet.add(numeroPlaza);
                            Collections.sort(dataSet);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Error obteniendo datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean areAllFieldsCompleted(View rootView) {
        ChipGroup chipGroup = rootView.findViewById(R.id.chipGroup);
        EditText etDate = rootView.findViewById(R.id.et_date);
        EditText etTimeEntry = rootView.findViewById(R.id.et_time_entry);
        EditText etTimeExit = rootView.findViewById(R.id.et_time_exit);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewChips);
        PlazaAdapter adapter = (PlazaAdapter) recyclerView.getAdapter();
        boolean isAnyRecyclerViewChipSelected = false;

        for (int i = 0; i < adapter.getItemCount(); i++) {
            PlazaAdapter.PlazaViewHolder holder = (PlazaAdapter.PlazaViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (holder != null && adapter.isItemSelected() ) {
                selectedChip = adapter.getSelectedItem();
                isAnyRecyclerViewChipSelected = true;
                break;
            }
        }

        if (!isAnyRecyclerViewChipSelected) {
            return false;
        }
        if (chipGroup.getCheckedChipId() == View.NO_ID) {
            return false;
        }
        if (etDate.getText().toString().isEmpty()) {
            return false;
        }
        if (etTimeEntry.getText().toString().isEmpty()) {
            return false;
        }
        if (etTimeExit.getText().toString().isEmpty()) {
            return false;
        }

        return true;
    }
    public void addReservation(){
        View rootView = getView();
        EditText etDate = rootView.findViewById(R.id.et_date);
        EditText etTimeEntry = rootView.findViewById(R.id.et_time_entry);
        EditText etTimeExit = rootView.findViewById(R.id.et_time_exit);

        Reservation reservation = new Reservation(FirebaseAuth.getInstance().getCurrentUser().getUid(), selectedChip, etDate.getText().toString(), etTimeEntry.getText().toString(), etTimeExit.getText().toString(), selectedChipType);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reservations")
                .add(reservation);
    }

    public String getCHipType(String chipText){
        if (Objects.equals(chipText, "Automovil")){
            return "STD";
        } else if (Objects.equals(chipText, "Motocicleta")) {
            return "MOTO";
        } else if (Objects.equals(chipText, "Estación de carga")) {
            return "ELEC";
        } else if (Objects.equals(chipText, "Movilidad reducida")) {
            return "DISC";
        }else {
            return "STD";
        }
    }
    public void createDialog(View rootView) {
        EditText etDate = rootView.findViewById(R.id.et_date);
        EditText etTimeEntry = rootView.findViewById(R.id.et_time_entry);
        EditText etTimeExit = rootView.findViewById(R.id.et_time_exit);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirmar reserva")
                .setMessage("¿Quieres confirmar tu reserva de tipo " + selectedChipType + "?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addReservation();
                        Bundle bundle = new Bundle();
                        System.out.println(bundle);
                        bundle.putString("chipType", selectedChipType);
                        bundle.putString("chip", selectedChip);
                        bundle.putString("date", etDate.getText().toString());
                        bundle.putString("entry", etTimeEntry.getText().toString());
                        bundle.putString("exit", etTimeExit.getText().toString());

                        navController.navigate(R.id.confirmarReservaFragment, bundle);
                    }
                })
                .show();
    }

}
