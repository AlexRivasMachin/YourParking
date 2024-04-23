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

import com.lksnext.arivas.R;
import com.lksnext.arivas.viewmodel.reservas.ReservasViewModel;

public class RealizarReservaFragment extends Fragment {

    private NavController navController;
    private ReservasViewModel mViewModel;

    public static RealizarReservaFragment newInstance() {
        return new RealizarReservaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_realizar_reserva, container, false);

        // Obtén una referencia al botón en tu diseño
        Button continuarReservaButton = rootView.findViewById(R.id.btnContinuar);

        // Configura un OnClickListener para el botón
        continuarReservaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = NavHostFragment.findNavController(RealizarReservaFragment.this);
                    navController.navigate(R.id.confirmarReservaFragment);
                }
            }
        );

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
}
