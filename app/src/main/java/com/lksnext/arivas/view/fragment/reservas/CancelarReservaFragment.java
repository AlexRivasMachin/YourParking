package com.lksnext.arivas.view.fragment.reservas;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lksnext.arivas.viewmodel.reservas.CancelarReservaViewModel;
import com.lksnext.arivas.R;

public class CancelarReservaFragment extends Fragment {

    private CancelarReservaViewModel mViewModel;

    public static CancelarReservaFragment newInstance() {
        return new CancelarReservaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cancelar_reserva, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CancelarReservaViewModel.class);
        // TODO: Use the ViewModel
    }

}