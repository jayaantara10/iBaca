package id.jayaantara.ibaca.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import id.jayaantara.ibaca.ViewDataTulisanBackupActivity;
import id.jayaantara.ibaca.localdatabase.DBHandler;
import id.jayaantara.ibaca.model.DataPaper;

public class AdapterDataBackup extends RecyclerView.Adapter<AdapterDataBackup.HolderData>{
    private Context context;
    private Cursor cursor;


    public AdapterDataBackup(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
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
        if(!cursor.moveToPosition(position)){
            return;
        }

        long id_paper = cursor.getLong((Integer) cursor.getColumnIndex(DBHandler.row_id_paper));
        String judul = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_judul));
        String jenis = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_jenis));
        String penulis = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_penulis));
        String link = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_link));
        String batasan_umur = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_batasan_umur));
        String deskripsi = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_deskripsi));
        String lisensi = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_lisensi));
        String tanggal = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_tanggal));
        long id_user = cursor.getLong((Integer) cursor.getColumnIndex(DBHandler.row_id_paper));

        holder.itemView.setTag(id_paper);
        holder.tv_judul.setText(judul);
        holder.tv_penulis.setText(penulis);
        holder.tv_jenis.setText(jenis);
        holder.tv_tanggal.setText(tanggal);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
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
                    Intent intent = new Intent(context, ViewDataTulisanBackupActivity.class);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void swapCursor(Cursor newCrusor) {
        if(cursor != null){
            cursor.close();
        }

        cursor = newCrusor;

        if(newCrusor != null){
            this.notifyDataSetChanged();
        }
    }

}
