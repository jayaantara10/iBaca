package id.jayaantara.ibaca;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import id.jayaantara.ibaca.localdatabase.DBHandler;

import static java.nio.file.Files.delete;

public class ViewDataTulisanBackupActivity extends AppCompatActivity {

    private TextView tv_judul, tv_jenis, tv_penulis, tv_tanggal, tv_umur, tv_deskripsi, tv_link;
    private String judul, jenis, penulis, link, tanggal, umur, deskripsi, lisensi;
    private Button btn_hapus;
    private long id_paper, id_user;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_tulisan_backup);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id_paper = extras.getLong("SELECTED_ID");
        dbHandler = new DBHandler(this);
        tv_judul = (TextView) findViewById(R.id.tv_judul);
        tv_jenis = (TextView) findViewById(R.id.tv_jenis);
        tv_penulis = (TextView) findViewById(R.id.tv_penulis);
        tv_tanggal = (TextView) findViewById(R.id.tv_tanggal);
        tv_umur = (TextView) findViewById(R.id.tv_umur);
        tv_deskripsi = (TextView) findViewById(R.id.tv_deskripsi);
        tv_link = (TextView) findViewById(R.id.tv_link);

        btn_hapus = findViewById(R.id.btn_hapus);
        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allertConfirmationDeletePaper();
            }
        });

        getData();
    }

    private void allertConfirmationDeletePaper() {
        TextView tv_msg, title_popup_allert;

        Button btn_okey, btn_cancel;

        final Dialog dialog = new Dialog(ViewDataTulisanBackupActivity.this);

        dialog.setContentView(R.layout.allert_confirmation);

        title_popup_allert = (TextView) dialog.findViewById(R.id.tv_title_popup);
        title_popup_allert.setText(R.string.str_delet_item);

        tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(R.string.str_delet_item_response);

        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBackupPaper();
                allertSuccess("Delete Paper Successfull");
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

    private void deleteBackupPaper(){
        dbHandler.deleteDataPaper(id_paper);
    }

    private void allertSuccess(String msg) {
        TextView tv_msg, title_popup_allert;

        Button btn_okey, btn_cancel;

        final Dialog dialog = new Dialog(ViewDataTulisanBackupActivity.this);

        dialog.setContentView(R.layout.allert_success);

        title_popup_allert = (TextView) dialog.findViewById(R.id.msg);
        title_popup_allert.setText(R.string.str_success);

        tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(R.string.str_success_response);

        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putLong("SELECTED_ID",id_user);
                Intent intent = new Intent(ViewDataTulisanBackupActivity.this, BackupActivity.class);
                intent.putExtras(extras);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getData() {
        Cursor cursor = dbHandler.getDataPaper(id_paper);
        if(cursor.moveToFirst()){

            judul = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_judul));
            jenis = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_jenis));
            penulis = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_penulis));
            link = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_link));
            lisensi = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_lisensi));
            umur = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_batasan_umur));
            deskripsi = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_deskripsi));
            tanggal = cursor.getString((Integer) cursor.getColumnIndex(DBHandler.row_tanggal));
            id_user = cursor.getLong((Integer) cursor.getColumnIndex(DBHandler.row_id_user));


            tv_judul.setText(judul);
            tv_jenis.setText(jenis);
            tv_penulis.setText(penulis);
            tv_tanggal.setText(tanggal);
            tv_umur.setText(umur);
            tv_deskripsi.setText(deskripsi);
            tv_link.setText(link);
        }
    }

    private void toDashboard() {
        Intent intent = new Intent(ViewDataTulisanBackupActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        toDashboard();
    }
}