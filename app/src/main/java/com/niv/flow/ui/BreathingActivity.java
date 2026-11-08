package com.niv.flow.ui;

import static com.niv.flow.utils.BreathingState.HOLDING;
import static com.niv.flow.utils.BreathingState.IDLE;
import static com.niv.flow.utils.BreathingState.INHALING;
import static com.niv.flow.utils.BreathingState.RETURNING;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.niv.flow.R;
import com.niv.flow.data.HelperDB;
import com.niv.flow.models.ActivityLog;
import com.niv.flow.ui.main.MainActivity;
import com.niv.flow.ui.views.BreathingCircleView;
import com.niv.flow.utils.BreathingState;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class BreathingActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    HelperDB helperDB;
    TextView tvBreathingInstructions, tvCountdownTimer;
    CountDownTimer countDownTimer;
    ImageButton btnExitBreathing;
    BreathingCircleView breathingCircleView;
    ActivityLog activityLog;
    String currentUserId, createdAt;
    int dayOfWeek, hourOfDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);
        initComponent();

        breathingCircleView.setBreathingListener(new BreathingCircleView.BreathingListener() {
            @Override
            public void onStateChanged(BreathingState previousState, BreathingState newState) {
                if (previousState==IDLE && newState==INHALING) {
                    tvBreathingInstructions.setText("Inhale...");
                    startCountdownTimer();
                } else if (previousState==INHALING && newState==HOLDING) {
                    tvBreathingInstructions.setText("Release when ready");
                    countDownTimer.cancel();
                    tvCountdownTimer.setText("...");
                } else if (previousState==INHALING && newState==RETURNING) {
                    tvBreathingInstructions.setText("Resetting...");
                    countDownTimer.cancel();
                    tvCountdownTimer.setText("...");
                } else if (previousState==HOLDING && newState==RETURNING) {
                    tvBreathingInstructions.setText("Exhale...");
                    startCountdownTimer();
                } else if (previousState==RETURNING && newState==IDLE) {
                    tvBreathingInstructions.setText("Press and hold to inhale...");
                    countDownTimer.cancel();
                    tvCountdownTimer.setText("4");
                }
            }

            @Override
            public void onBreathingCycleCompleted() {
                currentUserId = sharedPreferences.getString("current_user", "");
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                createdAt = LocalDateTime.now().format(formatter);
                dayOfWeek = LocalDateTime.now().get(WeekFields.of(Locale.US).dayOfWeek());
                hourOfDay = LocalTime.now().getHour();
                activityLog = new ActivityLog(currentUserId, "BREATHING", dayOfWeek, hourOfDay, createdAt);
                helperDB.addActivityLog(activityLog);
            }
        });

        btnExitBreathing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                Intent intent = new Intent(BreathingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startCountdownTimer() {
        tvCountdownTimer.setText("4");
        countDownTimer = new CountDownTimer(4000,1000) {
            @Override
            public void onFinish() {
            }

            @Override
            public void onTick(long l) {
                String currentCount = String.valueOf((int)(Math.ceil((double)l /1000)));
                tvCountdownTimer.setText(currentCount);
            }
        }.start();
    }

    private void initComponent() {
        helperDB = new HelperDB(this);
        sharedPreferences = getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        tvBreathingInstructions = findViewById(R.id.tvBreathingInstructions);
        tvCountdownTimer = findViewById(R.id.tvCountdownTimer);
        btnExitBreathing = findViewById(R.id.btnExitBreathing);
        breathingCircleView = findViewById(R.id.viewBreathingCircle);
    }
}