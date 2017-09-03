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
import com.martindisch.chronoscopy.logic.ChrUsage;

import java.util.List;

public class UsagesFragment extends Fragment {

    private RecyclerView mRvUsages;
    private UsageAdapter mAdapter;

    public UsagesFragment() {
        // Required empty public constructor
    }

    public static UsagesFragment newInstance() {
        return new UsagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usages, container, false);
        // Get views
        mRvUsages = (RecyclerView) view.findViewById(R.id.usages_rvUsages);

        // Prepare RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvUsages.setLayoutManager(layoutManager);
        mRvUsages.addItemDecoration(new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation()));
        mRvUsages.setHasFixedSize(true);

        // Set adapter
        List<ChrUsage> usages = ChrUsage.find(
                ChrUsage.class, null, null, null, "date DESC", null
        );
        mAdapter = new UsageAdapter(usages);
        mRvUsages.setAdapter(mAdapter);
        return view;
    }

}
