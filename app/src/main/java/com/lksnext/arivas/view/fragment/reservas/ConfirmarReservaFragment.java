package com.lksnext.arivas.view.fragment.reservas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lksnext.arivas.R;
import com.lksnext.arivas.viewmodel.reservas.ReservasViewModel;

public class ConfirmarReservaFragment extends Fragment {

    private NavController navController;
    private ReservasViewModel mViewModel;

    private TextView tvSelectedChipType;
    private TextView tvSelectedChip;
    private TextView tvReservationDate;
    private TextView tvTimeEntry;
    private TextView tvTimeExit;

    public static ConfirmarReservaFragment newInstance() {
        return new ConfirmarReservaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_confirmar_reserva, container, false);

        Button confirmarReservaButton = rootView.findViewById(R.id.btnConfirmarReserva);

        if (confirmarReservaButton != null) {
            confirmarReservaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = NavHostFragment.findNavController(ConfirmarReservaFragment.this);
                    navController.navigate(R.id.mainFragment);
                }
            });
        } else {
            // Manejar el caso donde el botón no se encuentra
            System.err.println("Botón btnConfirmarReserva no se encontró en el layout.");
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        View volverImage = view.findViewById(R.id.volverImageConfirmarReserva);
        if (volverImage != null) {
            volverImage.setOnClickListener(v -> navController.popBackStack(R.id.realizarReservaFragment, false));
        } else {
            // Manejar el caso donde volverImage no se encuentra
            System.err.println("View volverImageConfirmarReserva no se encontró en el layout.");
        }

        // Inicializar otros TextViews si son necesarios en esta parte del código
//        tvSelectedChipType = view.findViewById(R.id.tvSelectedChipType);
//        tvSelectedChip = view.findViewById(R.id.tvSelectedChip);
//        tvReservationDate = view.findViewById(R.id.tvReservationDate);
//        tvTimeEntry = view.findViewById(R.id.tvTimeEntry);
//        tvTimeExit = view.findViewById(R.id.tvTimeExit);

        // Verificar si los TextViews están correctamente referenciados
        if (tvSelectedChipType == null) {
            System.err.println("TextView tvSelectedChipType no se encontró en el layout.");
        }
        if (tvSelectedChip == null) {
            System.err.println("TextView tvSelectedChip no se encontró en el layout.");
        }
        if (tvReservationDate == null) {
            System.err.println("TextView tvReservationDate no se encontró en el layout.");
        }
        if (tvTimeEntry == null) {
            System.err.println("TextView tvTimeEntry no se encontró en el layout.");
        }
        if (tvTimeExit == null) {
            System.err.println("TextView tvTimeExit no se encontró en el layout.");
        }
    }
}
