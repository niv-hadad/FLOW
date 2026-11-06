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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ProfileFragment extends Fragment {

    HelperDB helperDB;
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
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("current_user");
                editor.apply();
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initiateData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("current_user", "");
        User currentUser = helperDB.getUserById(currentUserId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate pastDate = LocalDateTime.parse(currentUser.getCreatedAt(), formatter).toLocalDate();
        LocalDate todayDate = LocalDate.now();
        int days = (int) ChronoUnit.DAYS.between(pastDate, todayDate)+1;

        tvName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        tvEmail.setText(currentUser.getEmail());
        tvDaysInFlow.setText(String.valueOf(days) + " days");
        tvFlowState.setText(String.valueOf(currentUser.getFlowState()) + " days");
        if (currentUser.getWeeklyReflection() == null)
            tvWeeklyReflection.setText("Your journey is just beginning. Your first weekly reflection will appear here at the end of the week");
        else
            tvWeeklyReflection.setText(currentUser.getWeeklyReflection());

    }

    private void initComponent() {
        btnLogout = getView().findViewById(R.id.btnLogout);
        tvName = getView().findViewById(R.id.tvFullName);
        tvEmail = getView().findViewById(R.id.tvEmail);
        tvDaysInFlow = getView().findViewById(R.id.tvDaysInFlowSubtitle);
        tvFlowState = getView().findViewById(R.id.tvFlowStateSubtitle);
        tvWeeklyReflection = getView().findViewById(R.id.tvSageReflectionText);
        helperDB = new HelperDB(requireContext());
    }
}