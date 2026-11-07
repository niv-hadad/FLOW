package com.niv.flow.ui.main;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.niv.flow.R;
import com.niv.flow.data.HelperDB;
import com.niv.flow.models.User;
import com.niv.flow.utils.DailyInspiration;

import java.time.LocalTime;

public class HomeFragment extends Fragment {
    DailyInspiration dailyInspiration;
    HelperDB helperDB;
    TextView tvGreetingTime, tvGreetingName, tvDailyInspiration;
    View homeScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("current_user", "");
        User currentUser = helperDB.getUserById(currentUserId);
        tvGreetingName.setText(currentUser.getFirstName());
        if (sharedPreferences.getBoolean("daily_inspiration", true))
            tvDailyInspiration.setVisibility(View.VISIBLE);
        else
            tvDailyInspiration.setVisibility(View.INVISIBLE);
        tvDailyInspiration.setText(dailyInspiration.getDailyInspiration());
        changeDesignTime();
    }

    private void changeDesignTime() {
        int currentHour = LocalTime.now().getHour();

        tvGreetingTime.setTextColor(Color.parseColor("#25382D"));
        tvGreetingName.setTextColor(Color.parseColor("#25382D"));
        tvDailyInspiration.setTextColor(Color.parseColor("#66776B"));

        if (currentHour >= 6 && currentHour <= 11) {
            homeScreen.setBackgroundResource(R.drawable.bg_morning);
            tvGreetingTime.setText("Good morning,");
        } else if (currentHour >= 12 && currentHour <= 16) {
            homeScreen.setBackgroundResource(R.drawable.bg_afternoon);
            tvGreetingTime.setText("Good afternoon,");
        } else if (currentHour >= 17 && currentHour <= 20) {
            homeScreen.setBackgroundResource(R.drawable.bg_evening);
            tvGreetingTime.setText("Good evening,");
        } else if ((currentHour >= 21 && currentHour <= 23) || (currentHour >= 0 && currentHour <= 5)) {
            homeScreen.setBackgroundResource(R.drawable.bg_night);
            tvGreetingTime.setText("Good night,");
            tvGreetingTime.setTextColor(Color.parseColor("#FFFFFF"));
            tvGreetingName.setTextColor(Color.parseColor("#E8EDF5"));
            tvDailyInspiration.setTextColor(Color.parseColor("#D4DFEC"));
        }
    }

    private void initComponent() {
        helperDB = new HelperDB(requireContext());
        dailyInspiration = new DailyInspiration();
        tvGreetingTime = getView().findViewById(R.id.tvGreetingTime);
        tvGreetingName = getView().findViewById(R.id.tvGreetingName);
        tvDailyInspiration = getView().findViewById(R.id.tvDailyInspiration);
        homeScreen = getView().findViewById(R.id.homeScreen);
    }
}
