package com.niv.flow.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.niv.flow.R;
import com.niv.flow.adapters.WinsRecyclerAdapter;
import com.niv.flow.models.Win;
import com.niv.flow.models.Wins;

public class ProgressFragment extends Fragment {
    Wins wins;
    WinsRecyclerAdapter.RecycleViewClickListener listener;
    WinsRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // DESIGN PROTOTYPE:
        // Wins from database with RecyclerView integration not yet implemented.
        wins = Wins.getInstance();
        wins.add(new Win("Completed a workout", "Apr 24, 2025"));
        wins.add(new Win("Kept a consistent sleep schedule", "Apr 21, 2025"));
        wins.add(new Win("Participated in class", "Apr 20, 2025"));
        wins.add(new Win("Got A+ in Math", "Feb 12, 2025"));

        initWinsRecyclerView();
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

    private void itemClick() {
        listener = new WinsRecyclerAdapter.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                // open dialog fragment
            }
        };
    }
}
