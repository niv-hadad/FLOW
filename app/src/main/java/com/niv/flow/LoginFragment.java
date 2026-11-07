package com.niv.flow;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
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

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty() && password.isEmpty())
                showFlowSnackbar("Please enter your email address and password.");
            else if (email.isEmpty())
                showFlowSnackbar("Please enter your email address.");
            else if (password.isEmpty())
                showFlowSnackbar("Please enter your password.");
        } else {
            User user = helperDB.getUserByEmail(email);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("current_user", user.getUserId());
                    editor.apply();
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    showFlowSnackbar("Incorrect email or password.");
                }
            } else
                showFlowSnackbar("Incorrect email or password.");
        }
    }

    private void showFlowSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(
                requireView(),
                message,
                Snackbar.LENGTH_LONG
        );

        snackbar.getView().setBackgroundResource(R.drawable.snackbar_background);
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
        btnSubmitAuth = getView().findViewById(R.id.btnSubmitAuth);
        etEmail = getView().findViewById(R.id.etEmail);
        etPassword = getView().findViewById(R.id.etPassword);
        helperDB = new HelperDB(requireContext());
    }
}
