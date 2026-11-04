package com.niv.flow;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ProfileFragment profileFragment = new ProfileFragment();
    HomeFragment homeFragment = new HomeFragment();
    ProgressFragment progressFragment = new ProgressFragment();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setItemActiveIndicatorEnabled(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId==R.id.menu_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment,profileFragment).commit();
                    return true;
                } else if (itemId==R.id.menu_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment,homeFragment).commit();
                    return true;
                } else if (itemId==R.id.menu_progress) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment,progressFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }

    private void initComponent() {
        bottomNavigationView=findViewById(R.id.bottomNavigation);
    }
}