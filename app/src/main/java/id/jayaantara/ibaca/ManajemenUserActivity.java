package id.jayaantara.ibaca;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import id.jayaantara.ibaca.localdatabase.DBHandler;
import id.jayaantara.ibaca.userapi.Http;
import id.jayaantara.ibaca.userapi.LocalStorage;


public class ManajemenUserActivity extends AppCompatActivity {

    private EditText edt_name, edt_email, edt_password, edt_con_password;
    private Button btn_okey, btn_batal;
    private TextView tv_title;
    private View layout_email, layout_password, layout_con_password, layout_name;
    private String email, name, password, password_confirmation, status_login, old_password, new_password;
    private LocalStorage localStorage;
    private Boolean CHANGE_PASS_FLAG;
    private DBHandler dbHandler;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manajemen_user);

        dbHandler = new DBHandler(this);

        localStorage = new LocalStorage(ManajemenUserActivity.this);
        status_login = localStorage.getToken();

        layout_name = findViewById(R.id.layout_name);
        layout_email = findViewById(R.id.layout_email);
        layout_password = findViewById(R.id.layout_password);
        layout_con_password = findViewById(R.id.layout_con_password);

        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_con_password = (EditText) findViewById(R.id.edt_con_password);
        tv_title = findViewById(R.id.tv_title);


        btn_batal = findViewById(R.id.btn_batal);
        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManajemenUserActivity.this, DashboardActivity.class));
            }
        });

        Intent intent = getIntent();
        CHANGE_PASS_FLAG = intent.getBooleanExtra("CHANGE_PASS_FLAG", false);
        btn_okey = findViewById(R.id.btn_okey);
        if (status_login.isEmpty() == false && CHANGE_PASS_FLAG == false){
            tv_title.setText(getString(R.string.str_edit_user));
            layout_email.setVisibility(View.GONE);
            layout_password.setVisibility(View.GONE);
            layout_con_password.setVisibility(View.GONE);
            getUser();
            btn_okey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name = edt_name.getText().toString();
                    checkRegist();
                }
            });
        }else if(status_login.isEmpty() == false && CHANGE_PASS_FLAG == true){
            tv_title.setText(getString(R.string.str_change_pass));
            layout_email.setVisibility(View.GONE);
            layout_name.setVisibility(View.GONE);
            edt_password.setHint(R.string.str_old_pass);
            edt_con_password.setHint(R.string.str_new_pass);
            getUser();
            btn_okey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkRegist();
                }
            });
        }else{
            btn_okey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkRegist();
                }
            });
        }

    }


    // Validasi Input
    private void checkRegist(){
        if (status_login.isEmpty() == false && CHANGE_PASS_FLAG == false){
            if(edt_name.length() == 0 ){
                edt_name.setError("Email field is required");
                edt_name.requestFocus();
            }else{
                sendUpdate();
            }
        }else if(status_login.isEmpty() == false && CHANGE_PASS_FLAG == true){
            if(edt_password.length() == 0 ){
                edt_password.setError("Password field is required");
                edt_password.requestFocus();
            }else if(edt_con_password.length() == 0 ) {
                edt_con_password.setError("Please confirm your password");
                edt_con_password.requestFocus();
            }else{
                sendChangePass();
            }
        }else{
            if(edt_email.length() == 0 || Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches() == false){
                edt_email.setError("Email field is required");
                edt_email.requestFocus();
            }else if(edt_name.length() == 0 ){
                edt_name.setError("Namefield is required");
                edt_name.requestFocus();
            }else if(edt_password.length() == 0 ){
                edt_password.setError("Password field is required");
                edt_password.requestFocus();
            }else if(edt_con_password.length() == 0 ){
                edt_con_password.setError("Please confirm your password");
                edt_con_password.requestFocus();
            }else {
                String check_pass = edt_con_password.getText().toString();
                if (edt_password.getText().toString().equals(check_pass)) {
                    sendRegister();
                } else {
                    edt_con_password.setError("Confirmation password fail, you input different password");
                    edt_con_password.requestFocus();
                }
            }
        }

    }

    //Alert Builder
    private void allertUpdateSuccesfully() {
        TextView tv_title_popup, tv_email, tv_nama_user;

        Button btn_okey;

        final Dialog dialog = new Dialog(ManajemenUserActivity.this);

        dialog.setContentView(R.layout.allert_data_user_success);

        tv_title_popup = (TextView) dialog.findViewById(R.id.tv_title_popup);
        tv_title_popup.setText(getString(R.string.str_alert_success_user2));

        tv_email = (TextView) dialog.findViewById(R.id.tv_email);
        tv_email.setText(email);

        tv_nama_user = (TextView) dialog.findViewById(R.id.tv_name);
        tv_nama_user.setText(edt_name.getText().toString());


        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                toDashboard();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void allertChangePassSuccesfully() {
        TextView tv_title_popup, tv_email, tv_nama_user;

        Button btn_okey;

        final Dialog dialog = new Dialog(ManajemenUserActivity.this);

        dialog.setContentView(R.layout.allert_data_user_success);

        tv_title_popup = (TextView) dialog.findViewById(R.id.tv_title_popup);
        tv_title_popup.setText(getString(R.string.str_change_pass_success));

        tv_email = (TextView) dialog.findViewById(R.id.tv_email);
        tv_email.setText(email);

        tv_nama_user = (TextView) dialog.findViewById(R.id.tv_name);
        tv_nama_user.setText(name);


        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                logout();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void allertRegisterSuccesfully() {
        TextView tv_email, tv_nama_user;

        Button btn_okey;

        final Dialog dialog = new Dialog(ManajemenUserActivity.this);

        dialog.setContentView(R.layout.allert_data_user_success);

        tv_email = (TextView) dialog.findViewById(R.id.tv_email);
        tv_email.setText(edt_email.getText().toString());

        tv_nama_user = (TextView) dialog.findViewById(R.id.tv_name);
        tv_nama_user.setText(edt_name.getText().toString());


        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                toLogin();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void allertFail(String msg) {
        TextView tv_msg;

        Button btn_okey;

        final Dialog dialog = new Dialog(ManajemenUserActivity.this);

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

    //Api function buad transaksi data antara android dengan laravel
    private void sendRegister(){
        name = edt_name.getText().toString();
        email = edt_email.getText().toString();
        password = edt_password.getText().toString();
        password_confirmation = edt_con_password.getText().toString();
        JSONObject params = new JSONObject();
        try{
            params.put("name", name);
            params.put("email",email);
            params.put("password",password);
            params.put("password_confirmation", password_confirmation);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server)+"register";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(ManajemenUserActivity.this, url);
                http.setMethod("post");
                http.setData(data);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 201 || code == 200){
                            allertRegisterSuccesfully();
                        }
                        else if(code == 422){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                allertFail(msg);
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

    private void sendUpdate(){
        name = edt_name.getText().toString();
        JSONObject params = new JSONObject();
        try{
            params.put("name", name);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server)+"update";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(ManajemenUserActivity.this, url);
                http.setMethod("post");
                http.setToken(true);
                http.setData(data);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 201 || code == 200){
                            allertUpdateSuccesfully();
                        }
                        else if(code == 422){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                allertFail(msg);
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

    private void getUser(){
        String url = getString(R.string.api_server)+"user";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(ManajemenUserActivity.this, url);
                http.setToken(true);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                id = response.getLong("id");
                                name = response.getString("name");
                                email = response.getString("email");
                                edt_name.setText(name);

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

    private void sendChangePass(){
        old_password = edt_password.getText().toString();
        new_password = edt_con_password.getText().toString();
        JSONObject params = new JSONObject();
        try{
            params.put("old_password", old_password);
            params.put("new_password", new_password);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server)+"changePassword";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(ManajemenUserActivity.this, url);
                http.setMethod("post");
                http.setToken(true);
                http.setData(data);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 201 || code == 200){
                            allertChangePassSuccesfully();
                        }
                        else if(code == 422){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                allertFail(msg);
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

    //Function tambahan untuk beralih halaman
    private void logout() {
        String url = getString(R.string.api_server)+"logout";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(ManajemenUserActivity.this, url);
                http.setMethod("post");
                http.setToken(true);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            toLogout();
                        }else {
                            String msg_error = "ERROR: "+code;
                            allertFail(msg_error);
                        }
                    }
                });
            }
        }).start();
    }

    private void toLogout() {
        LocalStorage localStorage = new LocalStorage(ManajemenUserActivity.this);
        localStorage.deleteToken(ManajemenUserActivity.this);
        Intent intent = new Intent(ManajemenUserActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void toLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void toDashboard() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}