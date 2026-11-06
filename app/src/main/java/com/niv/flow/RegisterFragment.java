package com.niv.flow;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.niv.flow.models.ActivityLog;
import com.niv.flow.models.HelperDB;
import com.niv.flow.models.User;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
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
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createAccount() {
        firstName=etFirstName.getText().toString();
        lastName=etLastName.getText().toString();
        email=etEmail.getText().toString();
        password=etPassword.getText().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        createdAt=LocalDateTime.now().format(formatter);

        User user = new User(firstName,lastName,email,password,createdAt);
        helperDB.addUser(user);

        user = helperDB.getUserByEmail(email);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_user", user.getUserId());
        editor.apply();

        Set<String> chosenHours = sharedPreferences.getStringSet("onboarding_hours", Collections.emptySet());
        Set<String> chosenDays = sharedPreferences.getStringSet("onboarding_days", Collections.emptySet());

        for (String hour : chosenHours) {
            for (String day : chosenDays) {
                int hourNumber = Integer.parseInt(hour);
                int dayNumber = Integer.parseInt(day);
                ActivityLog activityLog = new ActivityLog(user.getUserId(),"ONBOARDING",dayNumber,hourNumber,createdAt);
                helperDB.addActivityLog(activityLog);
            }
        }
    }

    private void initComponent() {
        helperDB= new HelperDB(getContext());
        btnSubmitAuth=getView().findViewById(R.id.btnSubmitAuth);
        etFirstName=getView().findViewById(R.id.etFirstName);
        etLastName=getView().findViewById(R.id.etLastName);
        etEmail=getView().findViewById(R.id.etEmail);
        etPassword=getView().findViewById(R.id.etPassword);
    }
}