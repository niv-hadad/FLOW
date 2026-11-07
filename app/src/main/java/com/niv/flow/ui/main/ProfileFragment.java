package com.niv.flow.ui.main;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.niv.flow.R;
import com.niv.flow.data.HelperDB;
import com.niv.flow.models.User;
import com.niv.flow.ui.auth.AuthActivity;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private String pendingSetting;
    private MaterialSwitch pendingSwitch;
    HelperDB helperDB;
    MaterialSwitch swSmartReminders, swDailyInspiration, swDailyWinReminder;
    TextView tvName, tvEmail, tvDaysInFlow, tvFlowState, tvWeeklyReflection;
    MaterialButton btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent();
        initiateData();

        swSmartReminders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@androidx.annotation.NonNull CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    requestNotificationPermission("smart_reminders", swSmartReminders);
                else
                    setNewReminderSetting("smart_reminders", swSmartReminders, false);
            }
        });

        swDailyWinReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@androidx.annotation.NonNull CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    requestNotificationPermission("daily_win_reminder", swDailyWinReminder);
                else
                    setNewReminderSetting("daily_win_reminder", swDailyWinReminder, false);
            }
        });

        swDailyInspiration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@androidx.annotation.NonNull CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("daily_inspiration", isChecked);
                editor.apply();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("current_user");
                editor.apply();
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void initiateData() {
        String currentUserId = sharedPreferences.getString("current_user", "");
        User currentUser = helperDB.getUserById(currentUserId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate pastDate = LocalDateTime.parse(currentUser.getCreatedAt(), formatter).toLocalDate();
        LocalDate todayDate = LocalDate.now();
        int days = (int) ChronoUnit.DAYS.between(pastDate, todayDate) + 1;

        tvName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        tvEmail.setText(currentUser.getEmail());
        tvDaysInFlow.setText(days + " days");
        tvFlowState.setText(currentUser.getFlowState() + " days");
        if (currentUser.getWeeklyReflection() == null)
            tvWeeklyReflection.setText("Your journey is just beginning. Your first weekly reflection will appear here at the end of the week.");
        else
            tvWeeklyReflection.setText(currentUser.getWeeklyReflection());

        boolean notificationsAllowed = NotificationManagerCompat
                .from(requireContext()).areNotificationsEnabled();

        swSmartReminders.setChecked(sharedPreferences.getBoolean("smart_reminders", false) && notificationsAllowed);
        swDailyWinReminder.setChecked(sharedPreferences.getBoolean("daily_win_reminder", false) && notificationsAllowed);
        swDailyInspiration.setChecked(sharedPreferences.getBoolean("daily_inspiration", true));
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        setNewReminderSetting(pendingSetting, pendingSwitch, true);
                    } else {
                        setNewReminderSetting("smart_reminders", swSmartReminders, false);
                        setNewReminderSetting("daily_win_reminder", swDailyWinReminder, false);
                    }
                }
            }
    );

    private void requestNotificationPermission(String setting, MaterialSwitch settingSwitch) {
        boolean enabled = NotificationManagerCompat.from(requireContext()).areNotificationsEnabled();

        if (enabled) {
            setNewReminderSetting(setting, settingSwitch, true);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            pendingSetting = setting;
            pendingSwitch = settingSwitch;
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            setNewReminderSetting(setting, settingSwitch, false);
        }
    }

    private void setNewReminderSetting(String setting, MaterialSwitch settingSwitch, boolean checked) {
        if (settingSwitch.isChecked() != checked)
            settingSwitch.setChecked(checked);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(setting, checked);
        editor.apply();
    }

    private void initComponent() {
        sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        swDailyInspiration = getView().findViewById(R.id.swDailyInspiration);
        swDailyWinReminder = getView().findViewById(R.id.swDailyWinReminder);
        swSmartReminders = getView().findViewById(R.id.swSmartReminders);
        btnLogout = getView().findViewById(R.id.btnLogout);
        tvName = getView().findViewById(R.id.tvFullName);
        tvEmail = getView().findViewById(R.id.tvEmail);
        tvDaysInFlow = getView().findViewById(R.id.tvDaysInFlowSubtitle);
        tvFlowState = getView().findViewById(R.id.tvFlowStateSubtitle);
        tvWeeklyReflection = getView().findViewById(R.id.tvSageReflectionText);
        helperDB = new HelperDB(requireContext());
    }
}
