package com.niv.flow.ui.auth;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.niv.flow.R;
import com.niv.flow.data.HelperDB;
import com.niv.flow.models.ActivityLog;
import com.niv.flow.models.User;
import com.niv.flow.ui.main.MainActivity;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

public class RegisterFragment extends Fragment {
    HelperDB helperDB;
    MaterialButton btnSubmitAuth;
    EditText etFirstName, etLastName, etEmail, etPassword;
    String firstName, lastName, email, password, createdAt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent();
        btnSubmitAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        firstName = etFirstName.getText().toString().trim();
        lastName = etLastName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            showFlowSnackbar("Please enter your name.");
            return;
        } else if (email.isEmpty() && password.isEmpty()) {
            showFlowSnackbar("Please enter your email address and password.");
            return;
        } else if (email.isEmpty()) {
            showFlowSnackbar("Please enter your email address.");
            return;
        } else if (password.isEmpty()) {
            showFlowSnackbar("Please enter your password.");
            return;
        }

        if (helperDB.getUserByEmail(email) != null) {
            showFlowSnackbar("This email is already registered.");
            return;
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        createdAt = LocalDateTime.now().format(formatter);
        User user = new User(firstName, lastName, email, password, createdAt);
        helperDB.addUser(user);

        user = helperDB.getUserByEmail(email);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        Set<String> chosenHours = sharedPreferences.getStringSet("onboarding_hours", Collections.emptySet());
        Set<String> chosenDays = sharedPreferences.getStringSet("onboarding_days", Collections.emptySet());

        for (String hour : chosenHours) {
            for (String day : chosenDays) {
                int hourNumber = Integer.parseInt(hour);
                int dayNumber = Integer.parseInt(day);
                ActivityLog activityLog = new ActivityLog(user.getUserId(), "ONBOARDING", dayNumber, hourNumber, createdAt);
                helperDB.addActivityLog(activityLog);
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_user", user.getUserId());
        editor.remove("onboarding_hours");
        editor.remove("onboarding_days");
        editor.apply();

        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void showFlowSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(
                requireView(),
                message,
                Snackbar.LENGTH_LONG
        );

        snackbar.getView().setBackgroundResource(R.drawable.bg_snackbar);
        snackbar.setBackgroundTint(Color.parseColor("#75718D77"));
        snackbar.setTextColor(Color.parseColor("#25382D"));
        snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_FADE);
        TextView snackbarText = snackbar.getView().findViewById(
                com.google.android.material.R.id.snackbar_text
        );

        snackbarText.setTypeface(
                ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        );
        snackbarText.setTextSize(13f);
        View snackbarView = snackbar.getView();
        snackbarView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams params =
                        (ViewGroup.MarginLayoutParams) snackbar.getView().getLayoutParams();
                float density = getResources().getDisplayMetrics().density;

                params.leftMargin = (int) (32 * density);
                params.rightMargin = (int) (32 * density);
                params.bottomMargin = (int) (73 * density);

                snackbar.getView().setLayoutParams(params);
            }
        });
        snackbar.show();
    }

    private void initComponent() {
        helperDB = new HelperDB(getContext());
        btnSubmitAuth = getView().findViewById(R.id.btnSubmitAuth);
        etFirstName = getView().findViewById(R.id.etFirstName);
        etLastName = getView().findViewById(R.id.etLastName);
        etEmail = getView().findViewById(R.id.etEmail);
        etPassword = getView().findViewById(R.id.etPassword);
    }
}
