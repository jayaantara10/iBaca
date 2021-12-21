package id.jayaantara.ibaca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import id.jayaantara.ibaca.fragment.DataFragment;
import id.jayaantara.ibaca.fragment.HomeFragment;
import id.jayaantara.ibaca.fragment.UserFragment;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selected_fragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selected_fragment = new HomeFragment();
                        break;
                    case R.id.nav_data:
                        selected_fragment = new DataFragment();
                        break;
                    case R.id.nav_user:
                        selected_fragment = new UserFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }
}