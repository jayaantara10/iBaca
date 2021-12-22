package id.jayaantara.ibaca.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import id.jayaantara.ibaca.R;
import id.jayaantara.ibaca.ViewDataTulisanActivity;
import id.jayaantara.ibaca.model.DataPaper;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context context;
    private List<DataPaper> listPaper;
    private int flag;


    public AdapterData(Context context, List<DataPaper> listPaper, int flag) {
        this.context = context;
        this.listPaper = listPaper;
        this.flag = flag;
    }

    @NonNull
    @NotNull
    @Override
    public HolderData onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data, parent, false);
        HolderData holder = new HolderData(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HolderData holder, int position) {
        DataPaper paper = listPaper.get(position);
        long id = paper.getId();
        holder.itemView.setTag(id);
        holder.tv_judul.setText(paper.getJudul());
        holder.tv_penulis.setText(paper.getPenulis());
        holder.tv_jenis.setText(paper.getJenis());
        holder.tv_tanggal.setText(paper.getCreated_at());

    }

    @Override
    public int getItemCount() {
        return listPaper.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView tv_judul, tv_penulis, tv_jenis, tv_tanggal;

        public HolderData(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_judul = itemView.findViewById(R.id.tv_judul);
            tv_penulis = itemView.findViewById(R.id.tv_penulis);
            tv_jenis = itemView.findViewById(R.id.tv_jenis);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long id = (long) itemView.getTag();

                    Bundle extras = new Bundle();
                    extras.putLong("SELECTED_ID",id);
                    extras.putInt("FLAG_FRAGMENT", flag);
                    Intent intent = new Intent(context, ViewDataTulisanActivity.class);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void filterList(ArrayList<DataPaper> filteredList){
        listPaper = filteredList;
        notifyDataSetChanged();
    }
}
