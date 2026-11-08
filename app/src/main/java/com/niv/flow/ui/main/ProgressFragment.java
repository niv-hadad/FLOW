package com.niv.flow.ui.main;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.niv.flow.R;
import com.niv.flow.adapters.WinsRecyclerAdapter;
import com.niv.flow.data.HelperDB;
import com.niv.flow.models.Win;
import com.niv.flow.models.Wins;
import com.niv.flow.ui.dialogs.AddWinBottomSheet;

import java.util.Currency;

public class ProgressFragment extends Fragment {
    SharedPreferences sharedPreferences;
    HelperDB helperDB;
    MaterialCardView cvAddWinBtn;
    Wins wins;
    WinsRecyclerAdapter.RecycleViewClickListener listener;
    WinsRecyclerAdapter adapter;
    String currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponent();
        initWinsRecyclerView();
        currentUser = sharedPreferences.getString("current_user", "");
        refreshWins();

        getChildFragmentManager().setFragmentResultListener(
                "win_saved",
                getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        refreshWins();
                    }
                }
        );

        cvAddWinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddWinBottomSheet bottomSheet = new AddWinBottomSheet();
                bottomSheet.show(getChildFragmentManager(), "AddWinBottomSheet");
            }
        });
    }

    private void initComponent() {
        wins = Wins.getInstance();
        helperDB = new HelperDB(requireContext());
        sharedPreferences = requireContext().getSharedPreferences("com.niv.flow.SharedPreferences", MODE_PRIVATE);
        cvAddWinBtn = getView().findViewById(R.id.cvAddWinBtn);
    }

    private void initWinsRecyclerView() {
        RecyclerView recyclerView = getView().findViewById(R.id.rvWinsList);
        itemClick();

        adapter = new WinsRecyclerAdapter(Wins.getInstance(), listener, getActivity().getApplicationContext());
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    private void refreshWins() {
        Wins wins = Wins.getInstance();

        wins.clear();
        wins.addAll(helperDB.getWinsByUserId(currentUser));

        adapter.updateList(wins);
    }
    private void itemClick() {
        listener = new WinsRecyclerAdapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                // open dialog fragment
            }
        };
    }
}
