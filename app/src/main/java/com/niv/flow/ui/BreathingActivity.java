package com.niv.flow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.niv.flow.R;
import com.niv.flow.ui.main.MainActivity;
import com.niv.flow.ui.views.BreathingCircleView;

public class BreathingActivity extends AppCompatActivity {
    ImageButton btnExitBreathing;
    BreathingCircleView viewBreathingCircle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);
        initComponent();

        btnExitBreathing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BreathingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        btnExitBreathing = findViewById(R.id.btnExitBreathing);
    }
}