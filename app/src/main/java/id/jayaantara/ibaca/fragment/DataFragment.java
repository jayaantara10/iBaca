package id.jayaantara.ibaca.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import id.jayaantara.ibaca.DashboardActivity;
import id.jayaantara.ibaca.ManajemenTulisanActivity;
import id.jayaantara.ibaca.ManajemenUserActivity;
import id.jayaantara.ibaca.R;
import id.jayaantara.ibaca.adapter.AdapterData;
import id.jayaantara.ibaca.model.DataPaper;
import id.jayaantara.ibaca.model.ResponseModel;
import id.jayaantara.ibaca.retrofit.ApiClient;
import id.jayaantara.ibaca.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataFragment extends Fragment {

    private RecyclerView rv_data;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<DataPaper> listPaper;
    private AlertDialog.Builder dialog;

    private FloatingActionButton btn_tambah;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_data, container, false);

        listPaper = new ArrayList<>();
        rv_data = root.findViewById(R.id.rv_data);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        rv_data.setLayoutManager(layoutManager);

        retrieveData();
        btn_tambah = root.findViewById(R.id.btn_tambah);
        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddData();
            }
        });

        return root;
    }

    public void retrieveData(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> getData = apiInterface.getDataPaper();
        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                String msg = response.body().getMessage();
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                listPaper = response.body().getValues();


            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Koneksi gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialog{
        adapter = new AdapterData(getActivity(), listPaper);
        rv_data.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setDialog(new AdapterData().Dialog() {
            @Override
            public void onClick(long id) {
                final CharSequence[] dialogItem = {"Lihat", "Edit", "Hapus"};
                dialog = new AlertDialog.Builder(DashboardActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(DashboardActivity.this, ViewDataSosmedActivity.class);
                                intent.putExtra(DBHandler.row_id_sosmed, id);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent2 = new Intent(DashboardActivity.this, ManajemenDataSosmedActivity.class);
                                intent2.putExtra(DBHandler.row_id_sosmed, id);
                                startActivity(intent2);
                                break;
                            case 2:
                                deleteData(id);
                                break;
                        }
                    }
                });
            }
        }
    }

    private void allertFail(String msg) {
        TextView tv_msg;

        Button btn_okey;

        final Dialog dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.allert_fail_popup);

        tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(msg);

        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void toAddData(){
        Intent intent= new Intent(getContext(), ManajemenTulisanActivity.class);
        startActivity(intent);
    }

}