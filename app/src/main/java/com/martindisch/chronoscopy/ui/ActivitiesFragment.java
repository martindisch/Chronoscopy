package com.martindisch.chronoscopy.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrActivity;
import com.martindisch.chronoscopy.logic.ChrIndividual;
import com.martindisch.chronoscopy.logic.ChrUsage;
import com.martindisch.chronoscopy.logic.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActivitiesFragment extends Fragment {

    private RecyclerView mRvActivities;
    private ActivityAdapter mAdapter;
    private TextView mTvScore, mTvTime, mTvEvaluation;

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
        // Get views
        mRvActivities = (RecyclerView) view.findViewById(R.id.activities_rvActivities);
        mTvScore = (TextView) view.findViewById(R.id.activities_tvToday);
        mTvTime = (TextView) view.findViewById(R.id.activities_tvSpent);
        mTvEvaluation = (TextView) view.findViewById(R.id.activities_tvEvaluation);

        // Prepare RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvActivities.setLayoutManager(layoutManager);
        mRvActivities.addItemDecoration(new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation()));
        mRvActivities.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Request from DB off UI thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Read activities from DB
                List<ChrActivity> activities = ChrActivity.find(
                        ChrActivity.class, null, null, null, "name ASC", null
                );
                // Build ChrIndividual from SharedPreferences
                ChrIndividual individual = Util.getIndividual(getContext());
                mAdapter = new ActivityAdapter(activities, individual, getContext());
                // Update RecyclerView in UI thread
                mRvActivities.post(new Runnable() {
                    @Override
                    public void run() {
                        mRvActivities.setAdapter(mAdapter);
                    }
                });

                // Read today's usages from DB
                String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                List<ChrUsage> usages = ChrUsage.find(ChrUsage.class, "date = ?", today);
                // Get spent time for the day
                final String todayTime = Util.getTimeTotal(usages);
                // Get score for the day
                double cumulScore = 0;
                for (ChrUsage usage : usages) {
                    // Get the activity for the usage
                    ChrActivity activity = ChrActivity.findById(
                            ChrActivity.class, usage.getActivityId());
                    cumulScore += individual.getScore(activity, usage.getTimeHours());
                }
                final double todayScore = cumulScore;
                // Update status texts for today
                mTvScore.post(new Runnable() {
                    @Override
                    public void run() {
                        mTvScore.setText(String.format("%.1f", todayScore));
                        mTvTime.setText(todayTime);
                        mTvEvaluation.setText("0 / 5");
                    }
                });
            }
        }).start();
    }
}
