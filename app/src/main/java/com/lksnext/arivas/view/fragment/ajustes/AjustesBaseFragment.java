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
import com.lksnext.arivas.viewmodel.ajustes.AjustesBaseViewModel;

public class AjustesBaseFragment extends Fragment {

    private AjustesBaseViewModel mViewModel;

    public static AjustesBaseFragment newInstance() {
        return new AjustesBaseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajustes_base, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AjustesBaseViewModel.class);
        // TODO: Use the ViewModel
    }

}