package com.lksnext.arivas.view.fragment.ajustes;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.ImageView;

import com.lksnext.arivas.R;
import com.lksnext.arivas.viewmodel.ajustes.AjustesModificarDatosViewModel;

public class AjustesModificarDatosFragment extends Fragment {

    private AjustesModificarDatosViewModel mViewModel;

    private NavController navController;

    public static AjustesModificarDatosFragment newInstance() {
        return new AjustesModificarDatosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes_modificar_datos, container, false);

        ImageView volverAjustesEliminarCuentaImage = view.findViewById(R.id.volverModificarDatosCuentaImage);

        volverAjustesEliminarCuentaImage.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(AjustesModificarDatosFragment.this);
            navController.navigate(R.id.action_ajustesFragment_to_ajustesModificarDatosFragment);
        });

        Button botonModificarDatos = view.findViewById(R.id.btnConfirmarCambios);

        botonModificarDatos.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(AjustesModificarDatosFragment.this);
            navController.navigate(R.id.action_ajustesFragment_to_ajustesModificarDatosFragment);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener el NavController
        navController = Navigation.findNavController(view);

        // Configurar OnClickListener para la imagen de volver
        view.findViewById(R.id.volverModificarDatosCuentaImage).setOnClickListener(v -> navController.popBackStack(R.id.ajustesFragment, false));

        // Configurar OnClickListener para el botón de confirmar cambios
        view.findViewById(R.id.btnConfirmarCambios).setOnClickListener(v -> navController.popBackStack(R.id.ajustesFragment, false));
    }

}