package com.niv.flow.ui.dialogs;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.niv.flow.R;
import com.niv.flow.adapters.WinsRecyclerAdapter;
import com.niv.flow.data.HelperDB;
import com.niv.flow.models.Win;
import com.niv.flow.models.Wins;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddWinBottomSheet extends BottomSheetDialogFragment {

    WinsRecyclerAdapter adapter;
    WinsRecyclerAdapter.RecycleViewClickListener listener;
    HelperDB helperDB;
    SharedPreferences sharedPreferences;
    EditText etDetailedWin;
    MaterialButton btnSaveWin;
    String winContent;
    String currentUserId;
    Wins wins;
    String createdAt;
    String winTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_win_bottom_sheet, container, false);
        initComponent(view);
            // dismiss(); will close the bottom sheet
        btnSaveWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                winContent = etDetailedWin.getText().toString();

                if (winContent.isEmpty()) {
                    showFlowSnackbar("Please enter your win");
                    return;
                }

                currentUserId=sharedPreferences.getString("current_user", "");
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                createdAt = LocalDateTime.now().format(formatter);
                Win win = new Win(currentUserId, "My Win", winContent, createdAt);
                helperDB.addWin(win);

                getParentFragmentManager().setFragmentResult(
                        "win_saved",
                        new Bundle()
                );

                dismiss();
            }
        });
        return view;
    }

    private void showFlowSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(
                requireView(),
                message,
                Snackbar.LENGTH_LONG
        );

        snackbar.getView().setBackgroundResource(R.drawable.bg_snackbar);
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
    private void initComponent(View view) {
        wins = Wins.getInstance();
        adapter = new WinsRecyclerAdapter(Wins.getInstance(), listener, getActivity().getApplicationContext());
        helperDB = new HelperDB(requireContext());
        sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        etDetailedWin = view.findViewById(R.id.etDetailedWin);
        btnSaveWin = view.findViewById(R.id.btnSaveWin);
    }
}