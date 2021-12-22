package id.jayaantara.ibaca;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import id.jayaantara.ibaca.model.GetResponseModel;
import id.jayaantara.ibaca.model.PostResponseModel;
import id.jayaantara.ibaca.retrofit.ApiClient;
import id.jayaantara.ibaca.retrofit.ApiInterface;
import id.jayaantara.ibaca.userapi.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManajemenTulisanActivity extends AppCompatActivity {
    private String str_jenis, judul, jenis, penulis, link, deskripsi, batasan_umur, lisensi;
    private long id_user;
    private TextView tv_batasan_umur, tv_syarat;
    private EditText edt_judul, edt_penulis, edt_link, edt_deskripsi;
    private Spinner spnr_jenis;
    private RadioButton rb_licensed, rb_no_license;
    private RadioGroup rb_license_status;
    private Button btn_okey, btn_cancel;
    private SeekBar sb_batasan_umur;
    private CheckBox cb_syarat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manajemen_tulisan);


        getUser();
        tv_syarat = (TextView) findViewById(R.id.tv_syarat);
        tv_batasan_umur = (TextView) findViewById(R.id.tv_batasan_umur);
        edt_judul = (EditText) findViewById(R.id.edt_judul_tulisan);
        edt_penulis = (EditText) findViewById(R.id.edt_penulis);
        edt_link = (EditText) findViewById(R.id.edt_link);
        edt_deskripsi = (EditText) findViewById(R.id.edt_deskripsi);

        spnr_jenis = (Spinner) findViewById(R.id.spnr_jenis);
        spnr_jenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_jenis = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ManajemenTulisanActivity.this,
                R.array.edit_jenis_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_jenis.setAdapter(adapter);
        rb_licensed = findViewById(R.id.rb_licensed);
        rb_no_license = findViewById(R.id.rb_unlicensed);
        rb_license_status = findViewById(R.id.rb_license_status);

        sb_batasan_umur = (SeekBar) findViewById(R.id.sb_batasan_umur);
        sb_batasan_umur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                tv_batasan_umur.setText(Integer.toString(progressChangedValue));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                tv_batasan_umur.setText(Integer.toString(progressChangedValue));
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_batasan_umur.setText(Integer.toString(progressChangedValue));
            }
        });

        cb_syarat = (CheckBox) findViewById(R.id.cb_syarat);

        btn_okey = findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAdd();
            }
        });

        btn_cancel = findViewById(R.id.btn_batal);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDashboard();
            }
        });

    }

    private void toDashboard() {
        Intent intent = new Intent(ManajemenTulisanActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void checkAdd() {
        if(edt_judul.length() == 0 ) {
            edt_judul.setError("You must add titlle on your paper");
            edt_judul.requestFocus();
        }else if(edt_penulis.length() == 0){
            edt_penulis.setError("You must add writer namen");
            edt_penulis.requestFocus();
        }else if(edt_link.length() == 0 || Patterns.WEB_URL.matcher(edt_link.getText().toString()).matches() == false){
            edt_link.setError("You must add link dowload your paper");
            edt_link.requestFocus();
        }else if(edt_deskripsi.length() == 0 ){
            edt_deskripsi.setError("You must add the deskription of your paper");
            edt_deskripsi.requestFocus();
        }else if(str_jenis.matches("Chose Tipe Paper")){
            Toast.makeText(ManajemenTulisanActivity.this, "You must Choose Tipe of Paper", Toast.LENGTH_SHORT).show();
            spnr_jenis.requestFocus();
        }else if(Integer.parseInt(tv_batasan_umur.getText().toString()) < 1){
            tv_batasan_umur.setError("You must add the age restriction");
            sb_batasan_umur.requestFocus();
            Toast.makeText(ManajemenTulisanActivity.this, "You must add the age restriction", Toast.LENGTH_SHORT).show();
        }else if(rb_license_status.getCheckedRadioButtonId() == -1) {
            Toast.makeText(ManajemenTulisanActivity.this, "You must choose license status", Toast.LENGTH_SHORT).show();
        }else if(cb_syarat.isChecked() == false) {
            Toast.makeText(ManajemenTulisanActivity.this, "You must be agree with term and condition", Toast.LENGTH_SHORT).show();
        }else{
            sendAddData();
        }
    }

    private void sendAddData() {
        judul = edt_judul.getText().toString();
        jenis = str_jenis;
        penulis = edt_penulis.getText().toString();
        link = edt_link.getText().toString();
        deskripsi = edt_deskripsi.getText().toString();
        batasan_umur = tv_batasan_umur.getText().toString()+" years";
        int id_selection = rb_license_status.getCheckedRadioButtonId();
        RadioButton selection_status = (RadioButton) findViewById(id_selection);
        lisensi = selection_status.getText().toString();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PostResponseModel> createData = apiInterface.createDataPaper(judul, jenis, penulis, link, lisensi, batasan_umur, deskripsi, id_user);
        createData.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                String msg = response.body().getMessage();
                allertAddDataSuccesfully();
            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                String error_message = "Connection Status" + t.getMessage();
                allertFail(error_message);
            }

        });
    }

    private void getUser(){
        String url = getString(R.string.api_server)+"user";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(ManajemenTulisanActivity.this, url);
                http.setToken(true);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                id_user = response.getLong("id");
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            String msg_error = "ERROR: "+code;
                            allertFail(msg_error);
                        }
                    }
                });
            }
        }).start();
    }

    private void allertFail(String msg) {
        TextView tv_msg;

        Button btn_okey;

        final Dialog dialog = new Dialog(ManajemenTulisanActivity.this);

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

    private void allertAddDataSuccesfully() {
        TextView tv_title_popup, tv_judul, tv_jenis, tv_penulis, tv_link, tv_status_paten, tv_batasan_umur, tv_deskripsi;

        Button btn_okey;

        final Dialog dialog = new Dialog(ManajemenTulisanActivity.this);

        dialog.setContentView(R.layout.allert_data_tulisan_success);

        tv_judul = (TextView) dialog.findViewById(R.id.tv_judul);
        tv_judul.setText(judul);

        tv_jenis = (TextView) dialog.findViewById(R.id.tv_jenis);
        tv_jenis.setText(jenis);

        tv_penulis = (TextView) dialog.findViewById(R.id.tv_penulis);
        tv_penulis.setText(penulis);

        tv_link = (TextView) dialog.findViewById(R.id.tv_link );
        tv_link .setText(link);

        tv_status_paten = (TextView) dialog.findViewById(R.id.tv_status_paten);
        tv_status_paten.setText(lisensi);

        tv_batasan_umur = (TextView) dialog.findViewById(R.id.tv_batasan_umur);
        tv_batasan_umur.setText(batasan_umur);

        tv_deskripsi = (TextView) dialog.findViewById(R.id.tv_deskripsi);
        tv_deskripsi.setText(deskripsi);


        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                toViewData();
//              toDashboard();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

     @RequiresApi(api = Build.VERSION_CODES.O)
     private void toViewData(){
         DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
         LocalDateTime now = LocalDateTime.now();
         Bundle extras = new Bundle();

         extras.putString("JUDUL",judul);
         extras.putString("JENIS", jenis);
         extras.putString("PENULIS",penulis);
         extras.putString("LINK", link);
         extras.putString("LISENSI",lisensi);
         extras.putString("BATASAN_UMUR", batasan_umur);
         extras.putString("DESKRIPSI", deskripsi);
         extras.putString("TANGGAL", dtf.format(now));
         Intent intent = new Intent(this, ViewDataTulisanActivity.class);
         intent.putExtras(extras);
         startActivity(intent);
         finish();
    }
}