package com.martindisch.chronoscopy.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrIndividual;
import com.martindisch.chronoscopy.logic.ChrUsage;
import com.martindisch.chronoscopy.logic.Util;

import java.util.List;

public class UsagesFragment extends Fragment {

    private RecyclerView mRvUsages;
    private UsageAdapter mAdapter;
    private List<ChrUsage> mUsages;

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
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Get usage that was swiped away
                ChrUsage toDelete = mUsages.get(position);
                // Delete the usage from DB
                ChrUsage.delete(toDelete);
                // Remove the usage from the list used by the adapter
                mUsages.remove(position);
                // Notify adapter of removal
                mRvUsages.getAdapter().notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(mRvUsages);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Request from DB off UI thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Read usages from DB
                mUsages = ChrUsage.find(
                        ChrUsage.class, null, null, null, "date DESC, id DESC", null
                );
                // Build ChrIndividual from SharedPreferences
                ChrIndividual individual = Util.getIndividual(getContext());
                mAdapter = new UsageAdapter(mUsages, individual);
                // Update RecyclerView in UI thread
                mRvUsages.post(new Runnable() {
                    @Override
                    public void run() {
                        mRvUsages.setAdapter(mAdapter);
                    }
                });
            }
        }).start();
    }
}
