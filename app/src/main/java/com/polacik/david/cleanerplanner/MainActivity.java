package com.polacik.david.cleanerplanner;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.polacik.david.cleanerplanner.fragments.ClientsFragment;
import com.polacik.david.cleanerplanner.fragments.HistoryFragment;
import com.polacik.david.cleanerplanner.fragments.PaymentsFragment;
import com.polacik.david.cleanerplanner.fragments.SettingsFragment;
import com.polacik.david.cleanerplanner.fragments.WorksheetsFragment;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_frame_layout, new WorksheetsFragment()).commit();
        bottomNavigationView = findViewById(R.id.menu_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.menu_bottom_navigation_worksheets:
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, new WorksheetsFragment()).commit();
                    return true;

                case R.id.menu_bottom_navigation_history:
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, new HistoryFragment()).commit();
                    return true;

                case R.id.menu_bottom_navigation_payments:
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, new PaymentsFragment()).commit();
                    return true;

                case R.id.menu_bottom_navigation_clients:
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, new ClientsFragment()).commit();
                    return true;

                case R.id.menu_bottom_navigation_settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, new SettingsFragment()).commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, new WorksheetsFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.menu_bottom_navigation_worksheets);
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

}
