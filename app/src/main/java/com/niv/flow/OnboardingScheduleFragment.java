package com.niv.flow;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OnboardingScheduleFragment extends Fragment {
    ChipGroup cgDaysOfWeek, cgHoursOfDay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent();
        cgDaysOfWeek.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@androidx.annotation.NonNull ChipGroup chipGroup, @androidx.annotation.NonNull List<Integer> list) {
                saveScheduleData();
            }
        });
        cgHoursOfDay.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@androidx.annotation.NonNull ChipGroup chipGroup, @androidx.annotation.NonNull List<Integer> list) {
                saveScheduleData();
            }
        });
    }

    private void initComponent() {
        cgDaysOfWeek = getView().findViewById(R.id.cgDaysOfWeek);
        cgHoursOfDay = getView().findViewById(R.id.cgHoursOfDay);
    }

    private void saveScheduleData() {
        Set<String> chosenHours = new HashSet<>(), chosenDays = new HashSet<>();

        for (int chipId : cgDaysOfWeek.getCheckedChipIds()) {
            Chip chip = requireView().findViewById(chipId);
            chosenDays.add(chip.getTag().toString());
        }

        for (int chipId : cgHoursOfDay.getCheckedChipIds()) {
            Chip chip = requireView().findViewById(chipId);
            chosenHours.add(chip.getTag().toString());
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("onboarding_hours", chosenHours);
        editor.putStringSet("onboarding_days", chosenDays);
        editor.apply();
    }
}