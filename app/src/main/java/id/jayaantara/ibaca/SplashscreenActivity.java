package id.jayaantara.ibaca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import id.jayaantara.ibaca.userapi.LocalStorage;


public class SplashscreenActivity extends AppCompatActivity {

    private ProgressBar progress;
    private LocalStorage localStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        localStorage = new LocalStorage(this);
        progress = (ProgressBar) findViewById(R.id.progress_bar);

        new AsyncTask<Void, Integer, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                int progressStatus = 0;
                while (progressStatus < 5000) {
                    progressStatus++;
                    publishProgress(progressStatus);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progress.setProgress(values[0]);

            }
        }.execute();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String status_login = localStorage.getToken();
                if (status_login.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
//                startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class));
//                finish();
            }
        }, 5000L);
    }

}