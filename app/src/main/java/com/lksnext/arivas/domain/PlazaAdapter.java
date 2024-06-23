package com.lksnext.arivas.domain;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lksnext.arivas.R;

import java.util.List;

public class PlazaAdapter extends RecyclerView.Adapter<PlazaAdapter.PlazaViewHolder> {

    private List<Integer> dataSet;
    private Context context;
    private String chipType;

    public PlazaAdapter(List<Integer> dataSet, Context context, String chipType) {
        this.dataSet = dataSet;
        this.context = context;
        this.chipType = chipType;
    }

    public void updateDataSet(List<Integer> newDataSet) {
        dataSet.clear();
        dataSet.addAll(newDataSet);
        notifyDataSetChanged();
    }

    public void setChipType(String chipType) {
        this.chipType = chipType;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlazaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plaza, parent, false);
        return new PlazaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlazaViewHolder holder, int position) {
        Integer numeroReserva = dataSet.get(position);
        holder.bind(numeroReserva);

        int drawableId = getDrawableIdForChipType(chipType);
        holder.tipoReservaImagetView.setImageResource(drawableId);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class PlazaViewHolder extends RecyclerView.ViewHolder {

        private TextView numeroReservaTextView;
        private ImageView tipoReservaImagetView;

        public PlazaViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroReservaTextView = itemView.findViewById(R.id.NumReserva);
            tipoReservaImagetView = itemView.findViewById(R.id.tipoReserva);
        }

        public void bind(Integer numeroReserva) {
            numeroReservaTextView.setText(String.valueOf(numeroReserva));
        }
    }

    private int getDrawableIdForChipType(String chipType) {
        switch (chipType) {
            case "MOTO":
                return R.drawable.moto;
            case "DISC":
                return R.drawable.discapacitado;
            case "ELEC":
                return R.drawable.electrico;
            default:
                return R.drawable.auto;
        }
    }
}
