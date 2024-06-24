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

public class ReservaConfirmadaFragment extends Fragment {

    private NavController navController;
    private ReservasViewModel mViewModel;

    public static ReservaConfirmadaFragment newInstance() {
        return new ReservaConfirmadaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reserva_confirmada, container, false);

        Button verReservaButton = rootView.findViewById(R.id.btnVerReserva);
        verReservaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(ReservaConfirmadaFragment.this);
                navController.navigate(R.id.mainFragment);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        view.findViewById(R.id.volverImageReservaConfirmada).setOnClickListener(v -> navController.popBackStack(R.id.confirmarReservaFragment, false));
    }
}
