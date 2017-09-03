package com.martindisch.chronoscopy.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrActivity;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private Context mContext;
    private List<ChrActivity> mActivities;

    public ActivityAdapter(List<ChrActivity> mActivities, Context context) {
        this.mActivities = mActivities;
        this.mContext = context;
    }

    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_activity, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ActivityAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(mActivities.get(position).getName());
        holder.tvValue.setText("X.Y / h");
        holder.clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChrActivity activity = mActivities.get(holder.getAdapterPosition());
                Intent i = new Intent(mContext, NewActivityActivity.class);
                i.putExtra("name", activity.getName());
                i.putExtra("regret", activity.getRegret());
                i.putExtra("skill", activity.getSkill());
                i.putExtra("fun", activity.getFun());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActivities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvValue;
        private View clRoot;

        private ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_activity_tvName);
            tvValue = (TextView) itemView.findViewById(R.id.item_activity_tvValue);
            clRoot = itemView.findViewById(R.id.item_activity_clRoot);
        }
    }
}
