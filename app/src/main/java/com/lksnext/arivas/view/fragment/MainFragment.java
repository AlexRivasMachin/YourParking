package com.lksnext.arivas.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.lksnext.arivas.R;
import com.lksnext.arivas.view.activity.SettingsActivity;

public class MainFragment extends Fragment {

    private NavController navController;

    public MainFragment() {
        // Es necesario un constructor vacio
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Asignar la vista (layout) al fragmento
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button settingButton = rootView.findViewById(R.id.btnGoToSettings);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        navController.popBackStack(R.id.mainFragment, false);
    }
}
