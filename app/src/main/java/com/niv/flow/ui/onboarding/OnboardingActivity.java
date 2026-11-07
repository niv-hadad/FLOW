package com.niv.flow.ui.onboarding;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.niv.flow.R;
import com.niv.flow.adapters.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    ViewPager2 vpOnboarding;
    TabLayout tabIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        initComponent();

        List<Fragment> fragments = Arrays.asList(
                new OnboardingValue1Fragment(),
                new OnboardingValue2Fragment(),
                new OnboardingScheduleFragment(),
                new OnboardingPermissionsFragment()
        );

        ViewPagerAdapter adapter = new ViewPagerAdapter(
                fragments,
                getSupportFragmentManager(),
                getLifecycle()
        );

        vpOnboarding.setAdapter(adapter);

        new TabLayoutMediator(
                tabIndicator,
                vpOnboarding,
                (tab, position) -> {
                    tab.setIcon(R.drawable.bg_onboarding_dot_unselected);
                }
        ).attach();

        ViewGroup tabStrip =
                (ViewGroup) tabIndicator.getChildAt(0);

        for (int i = 0; i < tabStrip.getChildCount(); i++) {

            View dot = tabStrip.getChildAt(i);

            dot.setClickable(false);
            dot.setFocusable(false);
        }

        updateDots(vpOnboarding.getCurrentItem());

        vpOnboarding.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        updateDots(position);
                    }
                }
        );
    }

    private void updateDots(int selectedPosition) {
        for (int i = 0; i < tabIndicator.getTabCount(); i++) {

            TabLayout.Tab tab = tabIndicator.getTabAt(i);

            if (tab != null) {
                if (i == selectedPosition) {
                    tab.setIcon(R.drawable.bg_onboarding_dot_selected);
                } else {
                    tab.setIcon(R.drawable.bg_onboarding_dot_unselected);
                }
            }
        }
    }

    private void initComponent() {
        vpOnboarding = findViewById(R.id.vpOnboarding);
        tabIndicator = findViewById(R.id.tabIndicator);
    }
}
