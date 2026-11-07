package com.niv.flow.ui.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.niv.flow.R;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        bottomNavigationView.setItemActiveIndicatorEnabled(false);
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment, new HomeFragment()).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menu_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment, new ProfileFragment()).commit();
                    return true;
                } else if (itemId == R.id.menu_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment, new HomeFragment()).commit();
                    return true;
                } else if (itemId == R.id.menu_progress) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment, new ProgressFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

    private void initComponent() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }
}
