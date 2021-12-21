package id.jayaantara.ibaca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
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

public class LoginActivity extends AppCompatActivity {

    private EditText edt_email, edt_password;
    private TextView tv_register;
    private Button btn_login;
    private String email, password;
    private LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        localStorage = new LocalStorage((LoginActivity.this));


        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);

        tv_register =findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                toRegister();
            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                chekLogin();
            }
        });
    }


    // Alert Builder
    private void allertFail(String msg) {
        TextView tv_msg;

        Button btn_okey;

        final Dialog dialog = new Dialog(LoginActivity.this);

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

    //Validasi input
    public void chekLogin(){
        if(edt_email.length() == 0 || Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches() == false){
            edt_email.setError("Email field is required");
            edt_email.requestFocus();
        }else if(edt_password.length() == 0 ){
            edt_password.setError("Password is required");
            edt_password.requestFocus();
        }else{
            getLogin();
        }
    }

    //Api Login
    private void getLogin(){
        email = edt_email.getText().toString();
        password = edt_password.getText().toString();
        JSONObject params = new JSONObject();
        try{
            params.put("email", email);
            params.put("password", password);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server)+"login";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(LoginActivity.this, url);
                http.setMethod("post");
                http.setData(data);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if (code == 200){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                String token = response.getString("token");
                                localStorage.setToken(token);
                                toDashboard();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
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
                        else if(code == 401){
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

    //Fungsi tambahan perpindahan halaman
    private void toDashboard() {
        Intent intent= new Intent(getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void toRegister() {
        Intent intent= new Intent(getApplicationContext(), ManajemenUserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }


}