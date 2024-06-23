package com.lksnext.arivas.domain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.lksnext.arivas.R;
import com.lksnext.arivas.view.activity.MainActivity;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<Integer> dataSet;
    private FragmentManager fragmentManager;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView numReserva, fechaReserva, horaReserva, estadoReserva;
        public MaterialButton verReservaButton;

        public CardViewHolder(View view) {
            super(view);
            numReserva = view.findViewById(R.id.NumReserva);
            fechaReserva = view.findViewById(R.id.fechaReserva);
            horaReserva = view.findViewById(R.id.horaReserva);
            estadoReserva = view.findViewById(R.id.estadoReserva);
            verReservaButton = view.findViewById(R.id.outlinedButton2);
        }
    }

    public CardAdapter(FragmentManager fragmentManager ,List<Integer> dataSet) {
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
        holder.numReserva.setText(String.valueOf(dataSet.get(position)));

        // Configura los demás TextViews aquí si es necesario

        holder.verReservaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el BottomSheet al hacer clic en el botón
                BottomSheetDialogFragment bottomSheet = new ReservaBottomSheet();
                bottomSheet.show(fragmentManager, bottomSheet.getTag());
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
