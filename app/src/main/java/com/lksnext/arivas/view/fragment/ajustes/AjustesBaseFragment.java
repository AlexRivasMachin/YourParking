package com.lksnext.arivas.view.fragment.ajustes;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lksnext.arivas.R;
import com.lksnext.arivas.view.activity.LoginActivity;
import com.lksnext.arivas.view.activity.MainActivity;
import com.lksnext.arivas.viewmodel.ajustes.AjustesBaseViewModel;

public class AjustesBaseFragment extends Fragment {

    private AjustesBaseViewModel mViewModel;
    private MaterialSwitch notificacionesSwitch;

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
            createDeleteAccountDialog();
        });

        ImageView volverMainImage = view.findViewById(R.id.volverMainImage);
        volverMainImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        MaterialButton cerrarSesion = view.findViewById(R.id.cerrarSesion);
        cerrarSesion.setOnClickListener(v -> {
            logout(view);
        });

        notificacionesSwitch = view.findViewById(R.id.notificacionesSwitch);

        notificacionesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                activarNotificaciones();
            } else {
                desactivarNotificaciones();
            }
        });

        return view;
    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
    public void deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(requireContext(), "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Error al eliminar la cuenta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "No se encontró un usuario autenticado", Toast.LENGTH_SHORT).show();
        }
    }
    public void createDeleteAccountDialog() {
        Drawable icon = getResources().getDrawable(R.drawable.delete_acc);
        icon.setTint(ContextCompat.getColor(requireContext(), R.color.red_main));

        new MaterialAlertDialogBuilder(requireContext())
                .setIcon(icon)
                .setTitle("Confirmar eliminación de cuenta")
                .setMessage("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    deleteAccount();
                })
                .show();
    }
    private void activarNotificaciones() {
        Toast.makeText(requireContext(), "Notificaciones Activadas", Toast.LENGTH_SHORT).show();
    }

    private void desactivarNotificaciones() {
        Toast.makeText(requireContext(), "Notificaciones Desactivadas", Toast.LENGTH_SHORT).show();
    }
}
