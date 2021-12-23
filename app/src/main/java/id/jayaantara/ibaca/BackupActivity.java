package id.jayaantara.ibaca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import id.jayaantara.ibaca.adapter.AdapterDataBackup;
import id.jayaantara.ibaca.localdatabase.DBHandler;

public class BackupActivity extends AppCompatActivity {

    private RecyclerView rv_data;
    private AdapterDataBackup adapterDataBackup;
    private long id_user;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id_user = extras.getLong("SELECTED_ID");

        dbHandler = new DBHandler(this);

        rv_data = findViewById(R.id.rv_data);

        rv_data.setLayoutManager(new LinearLayoutManager(this));
        adapterDataBackup = new AdapterDataBackup(this, dbHandler.getDataPaperByidUser(id_user));
        rv_data.setAdapter(adapterDataBackup);

    }
}