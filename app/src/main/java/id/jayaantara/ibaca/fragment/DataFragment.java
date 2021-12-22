package id.jayaantara.ibaca.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.jayaantara.ibaca.ManajemenTulisanActivity;
import id.jayaantara.ibaca.R;
import id.jayaantara.ibaca.adapter.AdapterData;
import id.jayaantara.ibaca.model.DataPaper;
import id.jayaantara.ibaca.model.GetResponseModel;
import id.jayaantara.ibaca.retrofit.ApiClient;
import id.jayaantara.ibaca.retrofit.ApiInterface;
import id.jayaantara.ibaca.userapi.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataFragment extends Fragment {

    private long id_user;
    private String str_jenis = "All";
    private EditText search;
    private Spinner spnr_jenis;
    private RecyclerView rv_data;
    private AdapterData adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<DataPaper> listPaper;
    private SwipeRefreshLayout swl_data;
    private ProgressBar pb_data;
    private FloatingActionButton btn_tambah;
    private View root;
    private static int FLAG_FRAGMENT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_data, container, false);

        getUser();

        listPaper = new ArrayList<>();

        rv_data = root.findViewById(R.id.rv_data);
        swl_data = root.findViewById(R.id.swl_data);
        pb_data = root.findViewById(R.id.pb_data);;

        spnr_jenis = (Spinner) root.findViewById(R.id.spnr_jenis);
        spnr_jenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_jenis= adapterView.getItemAtPosition(i).toString();
                retrieveData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.jenis_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_jenis.setAdapter(adapter);
        swl_data.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swl_data.setRefreshing(true);
                retrieveData();
                swl_data.setRefreshing(false);
            }
        });

        search = (EditText) root.findViewById(R.id.edt_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterSerach(s.toString());
            }
        });


        btn_tambah = root.findViewById(R.id.btn_tambah);
        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddData();
            }
        });
        return root;
    }

    private void toAddData() {
        Intent intent = new Intent(getActivity(), ManajemenTulisanActivity.class);
        startActivity(intent);
    }

    public void retrieveData() {
        pb_data.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetResponseModel> getData = apiInterface.getDataPaper();
        getData.enqueue(new Callback<GetResponseModel>() {
            @Override
            public void onResponse(Call<GetResponseModel> call, Response<GetResponseModel> response) {
                String msg = response.body().getMessage();
                listPaper = response.body().getValues();
                rv_data.setLayoutManager(new LinearLayoutManager(getActivity()));
                filterByIdUser();
                if(str_jenis.matches("All") == false){
                    filterJenis(str_jenis);
                }
                adapter = new AdapterData(getActivity(), listPaper, FLAG_FRAGMENT);
                rv_data.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pb_data.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<GetResponseModel> call, Throwable t) {
                String error_message = "Connection Status" + t.getMessage();
                allertFail(error_message);
                pb_data.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void filterByIdUser(){
        ArrayList<DataPaper> filteredList = new ArrayList<DataPaper>();

        for (DataPaper data_paper : listPaper){
            if(data_paper.getId_user() == id_user){
                filteredList.add(data_paper);
            }
        }
        listPaper = filteredList;
    }

    private void filterSerach(String text){
        ArrayList<DataPaper> filteredList = new ArrayList<DataPaper>();

        for (DataPaper data_paper : listPaper){
            if(data_paper.getJudul().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(data_paper);
            }else if(data_paper.getJenis().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(data_paper);
            }else if(data_paper.getPenulis().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(data_paper);
            }
        }
        adapter.filterList(filteredList);
    }

    private void filterJenis(String text){
        ArrayList<DataPaper> filteredList = new ArrayList<DataPaper>();

        for (DataPaper data_paper : listPaper){
            if(data_paper.getJenis().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(data_paper);
            }
        }
        listPaper= filteredList;
    }


    private void getUser(){
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


    @Override
    public void onResume() {
        super.onResume();
        retrieveData();
    }
}