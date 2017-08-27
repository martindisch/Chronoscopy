package com.martindisch.chronoscopy.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrActivity;

import java.util.List;

public class ActivitiesFragment extends Fragment {

    private RecyclerView mRvActivities;
    private ActivityAdapter mAdapter;

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

        // Prepare RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvActivities.setLayoutManager(layoutManager);
        mRvActivities.addItemDecoration(new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation()));
        mRvActivities.setHasFixedSize(true);

        // Set adapter
        List<ChrActivity> activities = ChrActivity.find(
                ChrActivity.class, null, null, null, "name ASC", null
        );
        mAdapter = new ActivityAdapter(activities);
        mRvActivities.setAdapter(mAdapter);
        return view;
    }

}
