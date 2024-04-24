package com.lksnext.arivas.view.fragment.ajustes;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lksnext.arivas.R;
import com.lksnext.arivas.view.activity.MainActivity;
import com.lksnext.arivas.viewmodel.ajustes.AjustesBaseViewModel;

public class AjustesBaseFragment extends Fragment {

    private AjustesBaseViewModel mViewModel;

    public static AjustesBaseFragment newInstance() {
        return new AjustesBaseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes_base, container, false);

        TextView modificarDatos = view.findViewById(R.id.modificarDatos);
        modificarDatos.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.ajustesModificarDatosFragment);
        });

        TextView eliminarCuenta = view.findViewById(R.id.eliminarCuenta);
        eliminarCuenta.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.ajustesEliminarCuentaFragment);
        });

        ImageView volverMainImage = view.findViewById(R.id.volverMainImage);
        volverMainImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
;

        return view;
    }
}
