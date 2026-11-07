package com.niv.flow.ui.onboarding;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.niv.flow.R;
import com.niv.flow.ui.auth.AuthActivity;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class OnboardingPermissionsFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    MaterialButton btnSkipPermissions, btnEnableNotifications;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_permissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent();

        btnEnableNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        });
        btnSkipPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("smart_reminders", false);
                editor.putBoolean("daily_inspiration", true);
                editor.putBoolean("daily_win_reminder", false);
                editor.apply();

                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.putExtra("startWithSignup", 1);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (result) {
                        editor.putBoolean("smart_reminders", true);
                        editor.putBoolean("daily_win_reminder", true);
                    } else {
                        editor.putBoolean("smart_reminders", false);
                        editor.putBoolean("daily_win_reminder", false);
                    }
                    editor.putBoolean("daily_inspiration", true);
                    editor.apply();
                    Intent intent = new Intent(requireActivity(), AuthActivity.class);
                    intent.putExtra("startWithSignup", 1);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }
    );

    private void initComponent() {
        sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        btnSkipPermissions = getView().findViewById(R.id.btnSkipPermissions);
        btnEnableNotifications = getView().findViewById(R.id.btnEnableNotifications);
    }
}
