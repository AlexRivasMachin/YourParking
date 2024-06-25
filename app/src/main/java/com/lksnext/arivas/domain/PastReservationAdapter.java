package com.lksnext.arivas.domain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lksnext.arivas.R;

import java.util.List;
public class PastReservationAdapter extends RecyclerView.Adapter<PastReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservationList;
    private static Context context;

    public PastReservationAdapter(List<Reservation> reservationList, Context context) {
        this.reservationList = reservationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reserva_pasada, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.bind(reservation, context);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView reservationCard;
        private Chip pastReservationReservation;
        private Chip pastReservationDate;
        private Chip pastReservationIn;
        private Chip pastReservationOut;
        private Button eliminarButton;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            pastReservationReservation = itemView.findViewById(R.id.past_reservation_reservation);
            pastReservationDate = itemView.findViewById(R.id.past_reservation_date);
            pastReservationIn = itemView.findViewById(R.id.past_reservation_in);
            pastReservationOut = itemView.findViewById(R.id.past_reservation_out);
            eliminarButton = itemView.findViewById(R.id.eliminar_buttom);
        }

        public void bind(Reservation reservation, Context context) {

            if (pastReservationReservation != null) {
                pastReservationReservation.setText(reservation.getSlotId());
            }

            if (pastReservationDate != null) {
                pastReservationDate.setText(reservation.getDate());
            }

            if (pastReservationIn != null) {
                pastReservationIn.setText(reservation.getIn());
            }

            if (pastReservationOut != null) {
                pastReservationOut.setText(reservation.getOut());
            }

            eliminarButton.setOnClickListener(v -> {
                createDeleteDialog(reservation.getUid(), reservation.getType(), reservation.getDate(), reservation.getIn());
            });

            setChipIconType(pastReservationReservation, reservation.getType(), context);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void setChipIconType(Chip chip, String type, Context context) {
            if (chip == null) return;

            switch (type) {
                case "MOTO":
                    chip.setChipIcon(context.getDrawable(R.drawable.motociclea));
                    break;
                case "DISC":
                    chip.setChipIcon(context.getDrawable(R.drawable.discapacitado));
                    break;
                case "ELEC":
                    chip.setChipIcon(context.getDrawable(R.drawable.electrico));
                    break;
                default:
                    chip.setChipIcon(context.getDrawable(R.drawable.auto));
                    break;
            }
        }
    }
    public static void createDeleteDialog(String uid, String slotType, String date, String startTime) {
        new MaterialAlertDialogBuilder(context)
                .setIcon(R.drawable.delete_forever)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar la reserva?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    deleteReservation( uid, slotType, date, startTime);
                })
                .show();
    }

    public static void deleteReservation(String uid, String slotType, String date, String startTime) {
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
                                    Toast.makeText(context, "Reserva eliminada correctamente", Toast.LENGTH_SHORT).show();
                                    NavController navController = Navigation.findNavController(((Activity) context).getCurrentFocus());
                                    navController.navigate(R.id.mainFragment);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error al eliminar la reserva: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al buscar la reserva: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
