package com.niv.flow.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.niv.flow.R;

public class BreathingInfoBottomSheet extends BottomSheetDialogFragment {

    private MaterialButton btnGotIt;

    @Override
    public int getTheme() {
        return R.style.ThemeOverlay_FLOW_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.breathing_info_bottom_sheet, container, false);
        initComponent(view);
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    private void initComponent(View view) {
        btnGotIt = view.findViewById(R.id.btnGotIt);
    }
}
