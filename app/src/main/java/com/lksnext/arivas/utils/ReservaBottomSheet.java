package com.lksnext.arivas.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lksnext.arivas.R;

import java.util.Objects;

public class ReservaBottomSheet extends BottomSheetDialogFragment {

    String plaza;
    String tipoPlaza;
    String fecha;
    String horaEntrada;
    String horaSalida;
    TextView plazaText;
    TextView tipoPlazaText;
    TextView fechaText;
    TextView horaEntradaText;
    TextView horaSalidaText;
    ImageView tipoPlazaImage;
    ImageView tipiPLazaIcon;

    public ReservaBottomSheet() {
        // Constructor vacío
    }

    public ReservaBottomSheet(String plaza, String tipoPlaza, String fecha, String horaEntrada, String horaSalida) {
        this.plaza = plaza;
        this.tipoPlaza = tipoPlaza != null ? tipoPlaza : "STD";
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.reserva_bottom_sheet, container, false);

        plazaText= rootView.findViewById(R.id.reserva_plaza);
        tipoPlazaText= rootView.findViewById(R.id.reserva_tipo);
        fechaText = rootView.findViewById(R.id.reserva_fecha);
        horaEntradaText = rootView.findViewById(R.id.reserva_in);
        horaSalidaText = rootView.findViewById(R.id.reserva_out);
        tipoPlazaImage = rootView.findViewById(R.id.Bottom_sheet_type);
        tipiPLazaIcon = rootView.findViewById(R.id.reserva_tipo_icon);

        setImage(tipoPlazaImage, tipoPlaza);
        setIcon(tipiPLazaIcon, tipoPlaza);

        plazaText.setText("Número de plaza:   " + plaza);
        tipoPlazaText.setText("Tipo:   " + getTipoPlaza(tipoPlaza));
        fechaText.setText("Fecha de reserva:   " + fecha);
        horaEntradaText.setText("Hora de entrada:   " + horaEntrada);
        horaSalidaText.setText("Hora de salida:   " +horaSalida);

        rootView.findViewById(R.id.eliminar_reserva).setOnClickListener(v -> {
            createDeleteDialog(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        });

        return rootView;
    }

    private void setIcon(ImageView tipiPLazaIcon, String tipoPlaza) {
        if (tipoPlaza.equals("STD")) {
            tipiPLazaIcon.setImageResource(R.drawable.auto);
        } else if (tipoPlaza.equals("MOTO")) {
            tipiPLazaIcon.setImageResource(R.drawable.motociclea);
        } else if (tipoPlaza.equals("ELEC")) {
            tipiPLazaIcon.setImageResource(R.drawable.electrico);
        } else if (tipoPlaza.equals("DISC")) {
            tipiPLazaIcon.setImageResource(R.drawable.discapacitado);
        } else {
            tipiPLazaIcon.setImageResource(R.drawable.auto);
        }
    }

    private void setImage(ImageView tipoPlazaImage, String tipoPlaza) {
        if (tipoPlaza.equals("STD")) {
            tipoPlazaImage.setImageResource(R.drawable.coche_normal);
        } else if (tipoPlaza.equals("MOTO")) {
            tipoPlazaImage.setImageResource(R.drawable.moto);
        } else if (tipoPlaza.equals("ELEC")) {
            tipoPlazaImage.setImageResource(R.drawable.coche_electrico);
        } else if (tipoPlaza.equals("DISC")) {
            tipoPlazaImage.setImageResource(R.drawable.coche_minusvalido);
        } else {
            tipoPlazaImage.setImageResource(R.drawable.coche_normal);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public String getTipoPlaza(String tipoPlaza){
        if (tipoPlaza.equals("STD")) {
            return "Plaza estándar";
        } else if (tipoPlaza.equals("MOTO")) {
            return "Plaza de moto";
        } else if (tipoPlaza.equals("ELEC")) {
            return "Plaza con estación de carga";
        } else if (tipoPlaza.equals("DISC")) {
            return "Plaza para minusválidos";
        } else {
            return "Plaza estándar";
        }
    }

    public void createDeleteDialog(String uid) {
        new MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.delete_forever)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar la reserva?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    deleteReservation(uid, tipoPlaza, fecha, horaEntrada);
                })
                .show();
    }

    public void deleteReservation(String uid, String slotType, String date, String startTime) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("reservations")
                .whereEqualTo("uid", uid)
                .whereEqualTo("type", slotType)
                .whereEqualTo("date", date)
                .whereEqualTo("in", startTime)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        db.collection("reservations")
                                .document(document.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    this.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Error al eliminar la reserva: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error al buscar la reserva: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
