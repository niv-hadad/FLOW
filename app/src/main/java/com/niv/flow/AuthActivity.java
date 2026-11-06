package com.niv.flow;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class AuthActivity extends AppCompatActivity {

    LoginFragment loginFragment = new LoginFragment();
    RegisterFragment registerFragment = new RegisterFragment();

    boolean loginFlag=true;

    TextView tvToggleAuthClickable, tvToggleAuthTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initComponent();
        int startWithSignup = getIntent().getIntExtra("startWithSignup", 0);
        if (startWithSignup==1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.authFragmentContainer,registerFragment).commit();
            changeAuth();
        } else
            getSupportFragmentManager().beginTransaction().replace(R.id.authFragmentContainer,loginFragment).commit();
        tvToggleAuthClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAuth();
            }
        });
    }

    private void changeAuth() {
        loginFlag=!loginFlag;

        if (loginFlag) {
            getSupportFragmentManager().beginTransaction().replace(R.id.authFragmentContainer,loginFragment).commit();
            tvToggleAuthClickable.setText("Sign Up");
            tvToggleAuthTitle.setText("Don't have an account?");
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.authFragmentContainer,registerFragment).commit();
            tvToggleAuthClickable.setText("Sign In");
            tvToggleAuthTitle.setText("Already have an account?");
        }
    }

    private void initComponent() {
        tvToggleAuthClickable=findViewById(R.id.tvToggleAuthClickable);
        tvToggleAuthTitle=findViewById(R.id.tvToggleAuthTitle);
    }
}