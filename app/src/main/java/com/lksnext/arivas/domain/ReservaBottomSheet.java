package com.lksnext.arivas.domain;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

    public ReservaBottomSheet() {
        // Constructor vacío
    }

    public ReservaBottomSheet(String plaza, String tipoPlaza, String fecha, String horaEntrada, String horaSalida) {
        this.plaza = plaza;
        this.tipoPlaza = tipoPlaza;
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

        plazaText.setText("Número de plaza:   " + plaza);
        tipoPlazaText.setText("Tipo:   " + getTipoPlaza(tipoPlaza));
        fechaText.setText("Fecha de reserva:   " + fecha);
        horaEntradaText.setText("Hora de entrada:   " + horaEntrada);
        horaSalidaText.setText("Hora de salida:   " +horaSalida);

        rootView.findViewById(R.id.eliminar_reserva).setOnClickListener(v -> {
            createDeleteDialog(plaza, tipoPlaza, fecha, horaEntrada);
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Aquí puedes inicializar tus variables, si es necesario
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

    public void createDeleteDialog(String uid, String slotType, String date, String startTime) {
        new MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.delete_forever)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar la reserva?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    deleteReservation(uid, slotType, date, startTime);
                })
                .show();
    }

    public void deleteReservation(String uid, String slotType, String date, String startTime) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("reservations")
                .whereEqualTo("uid", uid)
                .whereEqualTo("slotType", slotType)
                .whereEqualTo("date", date)
                .whereEqualTo("startTime", startTime)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        db.collection("reservations")
                                .document(document.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(requireContext(), "Reserva eliminada correctamente", Toast.LENGTH_SHORT).show();
                                    NavController navController = Navigation.findNavController(Objects.requireNonNull(((Activity) requireContext()).getCurrentFocus()));
                                    navController.navigate(R.id.mainFragment);
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
