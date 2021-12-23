package id.jayaantara.ibaca.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import id.jayaantara.ibaca.BackupActivity;
import id.jayaantara.ibaca.ViewDataTulisanBackupActivity;
import id.jayaantara.ibaca.userapi.Http;
import id.jayaantara.ibaca.userapi.LocalStorage;
import id.jayaantara.ibaca.LoginActivity;
import id.jayaantara.ibaca.ManajemenUserActivity;
import id.jayaantara.ibaca.R;



public class UserFragment extends Fragment {


    private Button btn_edit, btn_hapus, btn_logout, btn_change_password, btn_list_backup;
    private TextView tv_email, tv_name;
    private String name, email, password;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_user, container, false);

        tv_email = (TextView) root.findViewById(R.id.tv_email);
        tv_name = (TextView) root.findViewById(R.id.tv_name);

        getUser();

        btn_edit = root.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEdit();
            }
        });

        btn_hapus = root.findViewById(R.id.btn_hapus);
        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allertConfirmationDeleteUser();
            }
        });

        btn_change_password = root.findViewById(R.id.btn_ganti_pass);
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toChangePass();
            }
        });
        
        btn_logout = root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allertConfirmationLogout();
            }
        });

        btn_list_backup = root.findViewById(R.id.btn_backup);
        btn_list_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBackupList();
            }
        });
        
        return root;

        
    }

    private void allertFail(String msg) {
        TextView tv_msg;

        Button btn_okey;

        final Dialog dialog = new Dialog(getActivity());

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
        dialog.show();
    }

    private void allertConfirmationDeleteUser() {
        TextView tv_msg, title_popup_allert;

        Button btn_okey, btn_cancel;

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.allert_confirmation);

        title_popup_allert = (TextView) dialog.findViewById(R.id.tv_title_popup);
        title_popup_allert.setText(R.string.str_delet_user);

        tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(R.string.str_delet_user_response);

        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
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

    private void allertConfirmationLogout() {
        TextView tv_msg, title_popup_allert;

        Button btn_okey, btn_cancel;

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.allert_confirmation);

        title_popup_allert = (TextView) dialog.findViewById(R.id.msg);
        title_popup_allert.setText(R.string.str_logout);

        tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(R.string.str_logout_response);

        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
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

    private void allertSuccess() {
        TextView tv_msg, title_popup_allert;

        Button btn_okey, btn_cancel;

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.allert_success);

        title_popup_allert = (TextView) dialog.findViewById(R.id.msg);
        title_popup_allert.setText(R.string.str_success);

        tv_msg = (TextView) dialog.findViewById(R.id.msg);
        tv_msg.setText(R.string.str_success_response);

        btn_okey = dialog.findViewById(R.id.btn_okey);
        btn_okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogout();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void getUser(){
        String url = getString(R.string.api_server)+"user";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(getActivity(), url);
                http.setToken(true);
                http.send();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                name = response.getString("name");
                                email = response.getString("email");
                                tv_name.setText(name);
                                tv_email.setText(email);
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

    private void logout() {
        String url = getString(R.string.api_server)+"logout";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(getActivity(), url);
                http.setMethod("post");
                http.setToken(true);
                http.send();

                getActivity().runOnUiThread(new Runnable() {
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

    private void deleteUser() {
        String url = getString(R.string.api_server)+"deleteUser";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(getActivity(), url);
                http.setMethod("post");
                http.setToken(true);
                http.send();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            allertSuccess();
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
        LocalStorage localStorage = new LocalStorage(getActivity());
        localStorage.deleteToken(getActivity());
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void toChangePass(){
        Intent intent = new Intent(getActivity(), ManajemenUserActivity.class);
        intent.putExtra("CHANGE_PASS_FLAG", true);
        startActivity(intent);
    }
    public void toEdit(){
        Intent intent = new Intent(getContext(), ManajemenUserActivity.class);
        startActivity(intent);
    }

    public void toBackupList(){
        Intent intent = new Intent(getContext(), BackupActivity.class);
        startActivity(intent);
    }
}