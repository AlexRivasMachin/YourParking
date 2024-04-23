package com.lksnext.arivas.view.fragment.reservas;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lksnext.arivas.R;
import com.lksnext.arivas.viewmodel.reservas.ReservasPasadasViewModel;

public class ReservasPasadasFragment extends Fragment {

    private ReservasPasadasViewModel mViewModel;

    public static ReservasPasadasFragment newInstance() {
        return new ReservasPasadasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservas_pasadas, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReservasPasadasViewModel.class);
        // TODO: Use the ViewModel
    }

}