package com.niv.flow;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class OnboardingPermissionsFragment extends Fragment {
    MaterialButton btnSkipPermissions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_permissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent();
        btnSkipPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        btnSkipPermissions=getView().findViewById(R.id.btnSkipPermissions);
    }
}