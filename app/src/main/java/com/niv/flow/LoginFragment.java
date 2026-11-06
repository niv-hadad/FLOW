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
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.niv.flow.models.HelperDB;
import com.niv.flow.models.User;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class LoginFragment extends Fragment {
    HelperDB helperDB;
    EditText etEmail, etPassword;
    String email, password;
    MaterialButton btnSubmitAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent();

        btnSubmitAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLogin();
            }
        });

    }

    private void validateLogin() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        User user = helperDB.getUserByEmail(email);
        if (user!=null) {
            if (user.getPassword().equals(password)) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("current_user", user.getUserId());
                editor.apply();
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            } else {
                // password incorrect error
            }
        } else {
            // Email not found error
        }
    }

    private void initComponent() {
        btnSubmitAuth=getView().findViewById(R.id.btnSubmitAuth);
        etEmail=getView().findViewById(R.id.etEmail);
        etPassword=getView().findViewById(R.id.etPassword);
        helperDB = new HelperDB(requireContext());
    }
}