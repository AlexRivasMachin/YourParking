package com.lksnext.arivas.view.fragment.ajustes;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.arivas.R;
import com.lksnext.arivas.viewmodel.ajustes.AjustesElminarCuentaViewModel;

public class AjustesElminarCuentaFragment extends Fragment {

    private NavController navController;

    private AjustesElminarCuentaViewModel mViewModel;

    public static AjustesElminarCuentaFragment newInstance() {
        return new AjustesElminarCuentaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes_elminar_cuenta, container, false);

        ImageView volverAjustesEliminarCuentaImage = view.findViewById(R.id.volverAjustesEliminarCuentaImage);

        Button eliminarCuentaButton = view.findViewById(R.id.eliminarCuenta);

        volverAjustesEliminarCuentaImage.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(AjustesElminarCuentaFragment.this);
            navController.navigate(R.id.action_ajustesFragment_to_ajustesEliminarCuentaFragment);
        });

        eliminarCuentaButton.setOnClickListener(v -> {
            Context context = requireContext();
            SharedPreferences prefs = context.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
            prefs.edit().clear().apply();

            FirebaseAuth.getInstance().signOut();
        });


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        view.findViewById(R.id.volverAjustesEliminarCuentaImage).setOnClickListener(v -> navController.popBackStack(R.id.ajustesFragment, false));
    }
}

