package com.lksnext.arivas.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.lksnext.arivas.domain.Reservation;

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
        private Chip pastReservationDate;
        private Chip pastReservationIn;
        private Button eliminarButton;
        private ImageView tipoImage;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            pastReservationDate = itemView.findViewById(R.id.past_reservation_date);
            pastReservationIn = itemView.findViewById(R.id.past_reservation_in);
            eliminarButton = itemView.findViewById(R.id.eliminar_buttom);
            tipoImage = itemView.findViewById(R.id.reservation_past_type);
        }

        public void bind(Reservation reservation, Context context) {

            if (pastReservationReservation != null) {
                pastReservationReservation.setText(reservation.getSlotId());
            }

            if (pastReservationDate != null) {
                pastReservationDate.setText(reservation.getDate());
            }

            if (pastReservationIn != null) {
                pastReservationIn.setText(reservation.getIn() + " - " + reservation.getOut());
            }

            eliminarButton.setOnClickListener(v -> {
                createDeleteDialog(reservation.getUid(), reservation.getType(), reservation.getDate(), reservation.getIn());
            });

            setChipIconType(tipoImage, reservation.getType(), context);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void setChipIconType(ImageView imageView, String type, Context context) {
            if (imageView == null) return;

            switch (type) {
                case "MOTO":
                    imageView.setImageResource(R.drawable.motociclea);
                    break;
                case "DISC":
                    imageView.setImageResource(R.drawable.discapacitado);
                    break;
                case "ELEC":
                    imageView.setImageResource(R.drawable.electrico);
                    break;
                default:
                    imageView.setImageResource(R.drawable.auto);
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
                                    Toast.makeText(context, "Reserva eliminada correctamente", Toast.LENGTH_SHORT).show();
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
