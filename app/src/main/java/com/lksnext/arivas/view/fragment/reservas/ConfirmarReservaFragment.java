package com.lksnext.arivas.view.fragment.reservas;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.chip.Chip;
import com.lksnext.arivas.R;
import com.lksnext.arivas.databinding.FragmentConfirmarReservaBinding;

public class ConfirmarReservaFragment extends Fragment {

    private NavController navController;
    private FragmentConfirmarReservaBinding binding;

    private String tvSelectedChipType;
    private String tvSelectedChip;
    private String tvReservationDate;
    private String tvTimeEntry;
    private String tvTimeExit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConfirmarReservaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        binding.btnConfirmarReserva.setOnClickListener(v -> {
            navController.navigate(R.id.mainFragment);
        });

        if (getArguments() != null) {
            tvSelectedChipType = getArguments().getString("chipType");
            tvSelectedChip = getArguments().getString("chip");
            tvReservationDate = getArguments().getString("date");
            tvTimeEntry = getArguments().getString("entry");
            tvTimeExit = getArguments().getString("exit");

            if (tvSelectedChipType != null) {
                setChip(binding.chipPlaza, tvSelectedChipType);
            }
            binding.tvFecha.setText(tvReservationDate);
            binding.tvHoraInicio.setText(tvTimeEntry);
            binding.tvHoraFin.setText(tvTimeExit);
        } else {
            Log.e("ConfirmarReservaFragment", "No arguments found.");
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setChip(Chip chip, String chipType) {
        chip.setText(chipType);
        switch (chipType) {
            case "STD":
                chip.setChipIcon(getResources().getDrawable(R.drawable.auto));
                break;
            case "ELEC":
                chip.setChipIcon(getResources().getDrawable(R.drawable.electrico));
                break;
            case "DISC":
                chip.setChipIcon(getResources().getDrawable(R.drawable.discapacitado));
                break;
            case "MOT0":
                chip.setChipIcon(getResources().getDrawable(R.drawable.motociclea));
                break;
            default:
                Log.e("ConfirmarReservaFragment", "Invalid chip type.");
                break;
        }
    }
}
