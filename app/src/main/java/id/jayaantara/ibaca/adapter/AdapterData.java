package id.jayaantara.ibaca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import id.jayaantara.ibaca.R;
import id.jayaantara.ibaca.model.DataPaper;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context context;
    private List<DataPaper> listPaper;
    private Dialog dialog;

    public interface Dialog{
        void onClick(long id);
    }

    public void setDialog(Dialog dialog){
        this.dialog = dialog;
    }


    public AdapterData(Context context, List<DataPaper> listPaper) {
        this.context = context;
        this.listPaper = listPaper;
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
                    long position = (long) itemView.getTag();
                    if(dialog!=null){
                        dialog.onClick(position);
                    }
                }
            });
        }
    }
}
