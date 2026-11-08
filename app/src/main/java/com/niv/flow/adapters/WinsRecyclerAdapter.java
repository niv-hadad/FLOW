package com.niv.flow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.niv.flow.R;
import com.niv.flow.models.Win;

import java.util.ArrayList;

// DESIGN PROTOTYPE:
// Wins from database with RecyclerView integration not yet implemented.
public class WinsRecyclerAdapter extends RecyclerView.Adapter<com.niv.flow.adapters.WinsRecyclerAdapter.MyViewHolder> {

    private ArrayList<Win> wins;
    Context context;
    private RecycleViewClickListener listener;

    public WinsRecyclerAdapter(ArrayList<Win> wins, RecycleViewClickListener listener, Context context) {
        this.wins = wins;
        this.context = context;
        this.listener = listener;
    }

    public void updateList(ArrayList<Win> newList) {
        this.wins = newList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvWinTitle;
        private TextView tvWinDate;

        public MyViewHolder(final View view) {
            super(view);

            tvWinTitle = view.findViewById(R.id.tvWinTitle);
            tvWinDate = view.findViewById(R.id.tvWinDate);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public com.niv.flow.adapters.WinsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.win_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.niv.flow.adapters.WinsRecyclerAdapter.MyViewHolder holder, int position) {
        Win win = wins.get(position);

        holder.tvWinTitle.setText(win.getWinTitle());
        holder.tvWinDate.setText(win.getCreatedAt());

    }

    @Override
    public int getItemCount() {
        return wins.size();
    }

    public interface RecycleViewClickListener {
        void onClick(View v, int position);
    }

}
