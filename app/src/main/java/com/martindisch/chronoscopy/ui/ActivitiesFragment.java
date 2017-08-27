package com.martindisch.chronoscopy.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrActivity;
import com.martindisch.chronoscopy.logic.ChrUsage;

import java.util.List;

public class ActivitiesFragment extends Fragment {

    private TextView mTvSpent;

    public ActivitiesFragment() {
        // Required empty public constructor
    }

    public static ActivitiesFragment newInstance() {
        return new ActivitiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activities, container, false);
        mTvSpent = (TextView) view.findViewById(R.id.activities_tvSpent);
        StringBuilder builder = new StringBuilder();
        List<ChrActivity> activities = ChrActivity.listAll(ChrActivity.class);
        for (ChrActivity activity : activities) {
            builder.append(
                    String.format("%d, %s, %f, %f, %f\n",
                            activity.getId(),
                            activity.getName(), activity.getRegret(),
                            activity.getSkill(), activity.getFun()));
        }
        builder.append("\n");
        List<ChrUsage> usages = ChrUsage.listAll(ChrUsage.class);
        for (ChrUsage usage : usages) {
            try {
                builder.append(String.format("%d, %s, %s\n",
                        usage.getActivityId(), usage.getDate(), usage.getTime()));
            } catch (Exception e) {
                builder.append("error\n");
            }
        }
        mTvSpent.setText(builder.toString());
        return view;
    }

}
