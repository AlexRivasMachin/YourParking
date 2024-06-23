package com.lksnext.arivas.domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lksnext.arivas.R;

import java.util.List;

public class PlazaAdapter extends RecyclerView.Adapter<PlazaAdapter.PlazaViewHolder> {

    private List<Integer> dataSet;
    private Context context;

    public PlazaAdapter(List<Integer> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
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

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //LinearLayout linearLayout = itemView.
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class PlazaViewHolder extends RecyclerView.ViewHolder {

        private TextView numeroReservaTextView;

        public PlazaViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroReservaTextView = itemView.findViewById(R.id.NumReserva);
        }

        public void bind(Integer numeroReserva) {
            numeroReservaTextView.setText(String.valueOf(numeroReserva));
        }
    }
}
