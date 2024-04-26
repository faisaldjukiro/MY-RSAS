package com.example.ovo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ovo.R;
import com.example.ovo.api.JasaListResponse;

import java.text.DecimalFormat;
import java.util.List;

public class JasaAdapter extends RecyclerView.Adapter<JasaAdapter.ViewHolder> {

    private List<JasaListResponse.Jasa> jasaList;
    private Context context;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mListener;

    // Metode untuk menetapkan listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public JasaAdapter(Context context, List<JasaListResponse.Jasa> jasaList) {
        this.context = context;
        this.jasaList = jasaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_jasa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        JasaListResponse.Jasa jasa = jasaList.get(position);
//        holder.tahun.setText(jasa.getTahun());
//
//        double jumlahDouble = Double.parseDouble(jasa.getJumlah());
//        DecimalFormat format = new DecimalFormat("#,###.#");
//        String formattedJumlah = "Rp " + format.format(jumlahDouble);
//
//        holder.jumlah.setText(formattedJumlah);
//        holder.bulan.setText(jasa.getBulan());

        JasaListResponse.Jasa jasa = jasaList.get(position);
        holder.tahun.setText(jasa.getTahun());
        // Tetapkan listener ke setiap item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });

        double jumlahDouble = Double.parseDouble(jasa.getJumlah());
        DecimalFormat format = new DecimalFormat("#,###.#");
        String formattedJumlah = "Rp " + format.format(jumlahDouble);

        holder.jumlah.setText(formattedJumlah);
        holder.bulan.setText(jasa.getBulan());
    }

    @Override
    public int getItemCount() {
        return jasaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tahun, jumlah, bulan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tahun = itemView.findViewById(R.id.tahun);
            jumlah = itemView.findViewById(R.id.jumlah);
            bulan = itemView.findViewById(R.id.bulan);
        }
    }

}
