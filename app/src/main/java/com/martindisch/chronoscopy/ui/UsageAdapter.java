package com.martindisch.chronoscopy.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrActivity;
import com.martindisch.chronoscopy.logic.ChrIndividual;
import com.martindisch.chronoscopy.logic.ChrUsage;

import java.util.List;

public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.ViewHolder> {

    private List<ChrUsage> mUsages;
    private ChrIndividual mIndividual;

    public UsageAdapter(List<ChrUsage> mUsages, ChrIndividual individual) {
        this.mUsages = mUsages;
        this.mIndividual = individual;
    }

    @Override
    public UsageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_usage, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UsageAdapter.ViewHolder holder, int position) {
        ChrActivity activity = ChrActivity.findById(
                ChrActivity.class, mUsages.get(position).getActivityId());
        holder.tvName.setText(activity.getName());
        holder.tvValue.setText(
                String.format("%.1f",
                        mIndividual.getScore(activity, mUsages.get(position).getTimeHours())));
        holder.tvTime.setText(mUsages.get(position).getTimeWorded());
        holder.clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: open activity activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvValue;
        private TextView tvTime;
        private View clRoot;

        private ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_usage_tvName);
            tvValue = (TextView) itemView.findViewById(R.id.item_usage_tvValue);
            tvTime = (TextView) itemView.findViewById(R.id.item_usage_tvTime);
            clRoot = itemView.findViewById(R.id.item_usage_clRoot);
        }
    }
}
