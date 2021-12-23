package id.jayaantara.ibaca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import id.jayaantara.ibaca.adapter.AdapterDataBackup;
import id.jayaantara.ibaca.localdatabase.DBHandler;

public class BackupActivity extends AppCompatActivity {

    private RecyclerView rv_data;
    private AdapterDataBackup adapterDataBackup;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        dbHandler = new DBHandler(this);

        rv_data = findViewById(R.id.rv_data);

        rv_data.setLayoutManager(new LinearLayoutManager(this));
        adapterDataBackup = new AdapterDataBackup(this, dbHandler.getAllDataPaper());
        rv_data.setAdapter(adapterDataBackup);

    }
}