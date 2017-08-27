package com.martindisch.chronoscopy.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrActivity;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private List<ChrActivity> mActivities;

    public ActivityAdapter(List<ChrActivity> mActivities) {
        this.mActivities = mActivities;
    }

    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_activity, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ActivityAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(mActivities.get(position).getName());
        holder.tvValue.setText("X.Y / h");
        // TODO: register OnClickListener
    }

    @Override
    public int getItemCount() {
        return mActivities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvValue;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_activity_tvName);
            tvValue = (TextView) itemView.findViewById(R.id.item_activity_tvValue);
        }
    }
}
