package id.jayaantara.ibaca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.jayaantara.ibaca.adapter.AdapterData;
import id.jayaantara.ibaca.localdatabase.DBHandler;
import id.jayaantara.ibaca.model.DataPaper;
import id.jayaantara.ibaca.model.GetResponseModel;
import id.jayaantara.ibaca.model.PostResponseModel;
import id.jayaantara.ibaca.retrofit.ApiClient;
import id.jayaantara.ibaca.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class ViewDataTulisanActivity extends AppCompatActivity {

    private TextView tv_judul, tv_jenis, tv_penulis, tv_tanggal, tv_umur, tv_deskripsi;
    private String judul, jenis, penulis, link, tanggal, umur, deskripsi, lisensi;
    private long id_paper, id_user;
    private Button btn_download, btn_hapus, btn_edit, btn_backup;
    private LinearLayout layout_lisensi, layout_btn_edt_dell, layout_btn_backup;
    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_tulisan);

        dbHandler = new DBHandler(this);
        tv_judul = (TextView) findViewById(R.id.tv_judul);
        tv_jenis = (TextView) findViewById(R.id.tv_jenis);
        tv_penulis = (TextView) findViewById(R.id.tv_penulis);
        tv_tanggal = (TextView) findViewById(R.id.tv_tanggal);
        tv_umur = (TextView) findViewById(R.id.tv_umur);
        tv_deskripsi = (TextView) findViewById(R.id.tv_deskripsi);

        layout_lisensi = findViewById(R.id.layout_lisensi);
        layout_btn_edt_dell = findViewById(R.id.layout_btn_edt_dell);
        layout_btn_backup = findViewById(R.id.layout_btn_backup);

        btn_download = findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openURL();
            }
        });

        btn_hapus = findViewById(R.id.btn_hapus);
        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allertConfirmationDeletePaper();
            }
        });

        btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEdit();
            }
        });

        btn_backup = findViewById(R.id.btn_backup);
        btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBackup();
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras.containsKey("SELECTED_ID") == true){
            if(extras.getInt("FLAG_FRAGMENT") == 2){
                layout_btn_edt_dell.setVisibility(View.VISIBLE);
                layout_btn_backup.setVisibility(View.VISIBLE);
            }else if (extras.getInt("FLAG_FRAGMENT") == 1){
                layout_btn_edt_dell.setVisibility(View.GONE);
                layout_btn_backup.setVisibility(View.GONE);
            }
            getDatumPaperApi();
        }else{
            layout_btn_edt_dell.setVisibility(View.GONE);
            layout_btn_backup.setVisibility(View.GONE);
            getDataIntent();
        }

    }

    private void backupToDB() {
        Toast.makeText(ViewDataTulisanActivity.this, ""+ id_paper , LENGTH_LONG).show();
        ContentValues values = new ContentValues();
        values.put(DBHandler.row_id_paper, id_paper);
        values.put(DBHandler.row_judul, judul);
        values.put(DBHandler.row_jenis, jenis);
        values.put(DBHandler.row_penulis, penulis);
        values.put(DBHandler.row_link, link);
        values.put(DBHandler.row_deskripsi, deskripsi);
        values.put(DBHandler.row_lisensi, lisensi);
        values.put(DBHandler.row_batasan_umur, umur);
        values.put(DBHandler.row_tanggal, tanggal);
        values.put(DBHandler.row_id_user, id_user);
        dbHandler.insertDataPaper(values);
        allertBackupSuccess();
    }

    private void updateBackupDB(){
        ContentValues values = new ContentValues();
        values.put(DBHandler.row_id_paper, id_paper);
        values.put(DBHandler.row_judul, judul);
        values.put(DBHandler.row_jenis, jenis);
        values.put(DBHandler.row_penulis, penulis);
        values.put(DBHandler.row_link, link);
        values.put(DBHandler.row_deskripsi, deskripsi);
        values.put(DBHandler.row_lisensi, lisensi);
        values.put(DBHandler.row_batasan_umur, umur);
        values.put(DBHandler.row_tanggal, tanggal);
        values.put(DBHandler.row_id_user, id_user);
        dbHandler.updateDataPaper(values, id_paper);
        allertBackupSuccess();
    }

    private void checkBackup(){
        boolean resultId = dbHandler.checkBackupPaperExist(id_paper);
        boolean resultData = dbHandler.checkBackupPaper(id_paper, judul, jenis, penulis, link, deskripsi, lisensi, umur, tanggal, id_user);
        if(resultId == true){
            if(resultData == true){
                allertFail("Data has been backup");
            }else{
                updateBackupDB();
            }
        }else{
            backupToDB();
        }


    }


    private void toEdit() {
        Intent intent = new Intent(ViewDataTulisanActivity.this, ManajemenTulisanActivity.class);
        intent.putExtra("SELECTED_ID", id_paper);
        startActivity(intent);
    }

    private void getDatumPaperApi(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id_paper = extras.getLong("SELECTED_ID");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PostResponseModel> getData = apiInterface.getDatumPaper(id_paper);
        getData.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                String msg = response.body().getMessage();
                DataPaper paper;
                paper = response.body().getValues();

                judul = paper.getJudul();
                jenis = paper.getJenis();
                penulis = paper.getPenulis();
                link = paper.getLink();
                lisensi = paper.getLisensi();
                umur = paper.getBatasan_umur();
                deskripsi = paper.getDeskripsi();
                tanggal = paper.getCreated_at();
                id_user = paper.getId_user();

                tv_judul.setText(judul);
                tv_jenis.setText(jenis);
                tv_penulis.setText(penulis);
                tv_tanggal.setText(tanggal);
                tv_umur.setText(umur);
                tv_deskripsi.setText(deskripsi);
                link = link;
                if(lisensi.matches("Unlicensed")){
                    layout_lisensi.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                String error_message = "Connection Status" + t.getMessage();
                allertFail(error_message);
            }
        });
    }

    private void deletePaper() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PostResponseModel> deleteData = apiInterface.deleteDataPaper(id_paper);
        deleteData.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                String msg = response.body().getMessage();
                allertSuccess("Delete Paper Successfull");
            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                String error_message = "Connection Status" + t.getMessage();
                allertFail(error_message);
            }
        });
    }

    private void allertSuccess(String msg) {
        TextView tv_msg, title_popup_allert;

        Button btn_okey, btn_cancel;

        final Dialog dialog = new Dialog(ViewDataTulisanActivity.this);

        dialog.setContentView(R.layout.allert_success);

        title_popup_allert = (TextView) dialog.findViewById(R.id.msg);
        title_popup_allert.setText(R.string.str_success);

        tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(R.string.str_success_response);

        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDashboard();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void allertBackupSuccess() {
        TextView tv_msg, title_popup_allert;

        Button btn_okey, btn_cancel;

        final Dialog dialog = new Dialog(ViewDataTulisanActivity.this);

        dialog.setContentView(R.layout.allert_success);

        title_popup_allert = (TextView) dialog.findViewById(R.id.msg);
        title_popup_allert.setText(R.string.str_success);

        tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(R.string.str_alert_success_backup);

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

    private void allertFail(String msg) {
        TextView tv_msg;

        Button btn_okey;

        final Dialog dialog = new Dialog(ViewDataTulisanActivity.this);

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

    private void openURL() {
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        judul = extras.getString("JUDUL");
        jenis = extras.getString("JENIS");
        penulis = extras.getString("PENULIS");
        link = extras.getString("LINK");
        lisensi = extras.getString("LISENSI");
        umur = extras.getString("BATASAN_UMUR");
        deskripsi = extras.getString("DESKRIPSI");
        tanggal = extras.getString("TANGGAL");

        tv_judul.setText(judul);
        tv_jenis.setText(jenis);
        tv_penulis.setText(penulis);
        tv_tanggal.setText(tanggal);
        tv_umur.setText(umur);
        tv_deskripsi.setText(deskripsi);


        if(lisensi.matches("Unlicensed")){
            layout_lisensi.setVisibility(View.GONE);
        }


    }

    private void allertConfirmationDeletePaper() {
        TextView tv_msg, title_popup_allert;

        Button btn_okey, btn_cancel;

        final Dialog dialog = new Dialog(ViewDataTulisanActivity.this);

        dialog.setContentView(R.layout.allert_confirmation);

        title_popup_allert = (TextView) dialog.findViewById(R.id.tv_title_popup);
        title_popup_allert.setText(R.string.str_delet_item);

        tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(R.string.str_delet_item_response);

        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePaper();
            }
        });

        btn_cancel = dialog.findViewById(R.id.btn_batal);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void toDashboard() {
        Intent intent = new Intent(ViewDataTulisanActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}