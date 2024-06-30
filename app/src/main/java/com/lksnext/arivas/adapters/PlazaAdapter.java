package com.lksnext.arivas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.lksnext.arivas.R;

import java.util.List;

public class PlazaAdapter extends RecyclerView.Adapter<PlazaAdapter.PlazaViewHolder> {

    private List<String> dataSet;
    private Context context;
    private String chipType;
    private int selectedItem = RecyclerView.NO_POSITION;

    public PlazaAdapter(List<String> dataSet, Context context, String chipType) {
        this.dataSet = dataSet;
        this.context = context;
        this.chipType = chipType;
    }

    public void updateDataSet(List<String> newDataSet) {
        dataSet.clear();
        dataSet.addAll(newDataSet);
        notifyDataSetChanged();
    }

    public void setChipType(String chipType) {
        this.chipType = chipType;
        notifyDataSetChanged();
    }

    public void setSelectedItem(int position) {
        int previousItem = selectedItem;
        selectedItem = position;
        notifyItemChanged(previousItem);
        notifyItemChanged(selectedItem);
    }

    public String getSelectedItem() {
        return dataSet.get(selectedItem);
    }
    public boolean isItemSelected() {
        return selectedItem != RecyclerView.NO_POSITION;
    }


    @NonNull
    @Override
    public PlazaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plaza, parent, false);
        return new PlazaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlazaViewHolder holder, int position) {
        String numeroReserva = dataSet.get(position);
        holder.bind(numeroReserva, chipType, position == selectedItem);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private int getDrawableIdForChipType(String chipType) {
        switch (chipType) {
            case "MOTO":
                return R.drawable.motociclea;
            case "DISC":
                return R.drawable.discapacitado;
            case "ELEC":
                return R.drawable.electrico;
            default:
                return R.drawable.auto;
        }
    }

    public class PlazaViewHolder extends RecyclerView.ViewHolder {

        private Chip reservationChip;

        public PlazaViewHolder(@NonNull View itemView) {
            super(itemView);
            reservationChip = itemView.findViewById(R.id.reservation_chip);

            reservationChip.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    setSelectedItem(position);
                }
            });
        }

        public void bind(String numeroReserva, String chipType, boolean isSelected) {
            reservationChip.setText(numeroReserva);
            int drawableId = getDrawableIdForChipType(chipType);
            reservationChip.setChipIconResource(drawableId);
            reservationChip.setChecked(isSelected);
        }
    }

}
