package com.lksnext.arivas.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.lksnext.arivas.R;
import com.lksnext.arivas.utils.ReservaBottomSheet;
import com.lksnext.arivas.domain.Reservation;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<Reservation> dataSet;
    private FragmentManager fragmentManager;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public Chip numReserva, fechaReserva, horaReserva;
        public MaterialCardView card;

        public CardViewHolder(View view) {
            super(view);
            numReserva = view.findViewById(R.id.reservation_reservation);
            fechaReserva = view.findViewById(R.id.reservation_date);
            horaReserva = view.findViewById(R.id.reservation_in);
            card = view.findViewById(R.id.reservation_card);
        }
    }

    public CardAdapter(FragmentManager fragmentManager, List<Reservation> dataSet) {
        this.dataSet = dataSet;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reserva, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if (dataSet != null && !dataSet.isEmpty()) {
            Reservation reservation = dataSet.get(position);

            if (reservation != null) {
                // Check for null before accessing reservation properties
                if (reservation.getSlotId() != null) {
                    holder.numReserva.setText(reservation.getSlotId());
                } else {
                    holder.numReserva.setText("");
                }

                if (reservation.getDate() != null) {
                    holder.fechaReserva.setText(reservation.getDate());
                } else {
                    holder.fechaReserva.setText("");
                }

                if (reservation.getIn() != null) {
                    holder.horaReserva.setText(reservation.getIn());
                } else {
                    holder.horaReserva.setText("");
                }

                String tipoDePlaza = reservation.getType();
                if (tipoDePlaza != null) {
                    switch (tipoDePlaza) {
                        case "STD":
                            holder.numReserva.setChipIconResource(R.drawable.auto);
                            break;
                        case "MOTO":
                            holder.numReserva.setChipIconResource(R.drawable.motociclea);
                            break;
                        case "ELEC":
                            holder.numReserva.setChipIconResource(R.drawable.electrico);
                            break;
                        case "DISC":
                            holder.numReserva.setChipIconResource(R.drawable.discapacitado);
                            break;
                        default:
                            holder.numReserva.setChipIconResource(R.drawable.parking);
                            break;
                    }
                    holder.numReserva.setText(tipoDePlaza);
                } else {
                    holder.numReserva.setText("STD");
                }

                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (reservation != null) {
                            BottomSheetDialogFragment bottomSheet = new ReservaBottomSheet(
                                    reservation.getSlotId(),
                                    reservation.getType(),
                                    reservation.getDate(),
                                    reservation.getIn(),
                                    reservation.getOut());
                            bottomSheet.show(fragmentManager, bottomSheet.getTag());
                        } else {
                            Log.e("CardAdapter", "Reservation object is null in onClick");
                        }
                    }
                });
            } else {
                Log.e("CardAdapter", "Reservation object is null at position: " + position);
            }
        } else {
            Log.e("CardAdapter", "DataSet is null or empty");
        }
    }

    @Override
    public int getItemCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

}
