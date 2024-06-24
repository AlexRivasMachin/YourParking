package com.lksnext.arivas.view.fragment.reservas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lksnext.arivas.R;
import com.lksnext.arivas.domain.CardAdapter;
import com.lksnext.arivas.viewmodel.reservas.ReservasViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReservasFragment extends Fragment {

    private NavController navController;
    private ReservasViewModel mViewModel;

    public static ReservasFragment newInstance() {
        return new ReservasFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservas, container, false);

        Button realizarReservaButton = rootView.findViewById(R.id.btnRealizarReserva);

        realizarReservaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(ReservasFragment.this);
                navController.navigate(R.id.realizarReservaFragment);
            }
        });

        return rootView;
    }
}