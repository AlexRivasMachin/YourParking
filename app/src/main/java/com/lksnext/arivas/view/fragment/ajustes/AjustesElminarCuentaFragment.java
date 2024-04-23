package com.lksnext.arivas.view.fragment.ajustes;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lksnext.arivas.R;
import com.lksnext.arivas.viewmodel.ajustes.AjustesElminarCuentaViewModel;

public class AjustesElminarCuentaFragment extends Fragment {

    private AjustesElminarCuentaViewModel mViewModel;

    public static AjustesElminarCuentaFragment newInstance() {
        return new AjustesElminarCuentaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajustes_elminar_cuenta, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AjustesElminarCuentaViewModel.class);
        // TODO: Use the ViewModel
    }

}