package com.martindisch.chronoscopy.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    private List<ChrActivity> mActivities;

    private OnActivitiesInteractionListener mListener;

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
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Get activity that was swiped away
                ChrActivity toDelete = mActivities.get(position);
                // Delete usages of this activity
                ChrUsage.deleteAll(ChrUsage.class, "activity_id = ?", toDelete.getId() + "");
                // Delete the activity from DB
                ChrActivity.delete(toDelete);
                // Remove the activity from the list used by the adapter
                mActivities.remove(position);
                // Notify adapter of removal
                mRvActivities.getAdapter().notifyItemRemoved(position);
                // Update UI
                updateUI();
                // Notify MainActivity of data change
                mListener.onActivitiesChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(mRvActivities);
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
                mActivities = ChrActivity.find(
                        ChrActivity.class, null, null, null, "name ASC", null
                );
                // Build ChrIndividual from SharedPreferences
                ChrIndividual individual = Util.getIndividual(getContext());
                mAdapter = new ActivityAdapter(mActivities, individual, getContext());
                // Update RecyclerView in UI thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRvActivities.setAdapter(mAdapter);
                    }
                });
            }
        }).start();
        updateUI();
    }

    /**
     * Updates the score, spent time and evaluation of the day.
     */
    public void updateUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Build ChrIndividual from SharedPreferences
                ChrIndividual individual = Util.getIndividual(getContext());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvScore.setText(String.format("%.1f", todayScore));
                        mTvTime.setText(todayTime);
                        mTvEvaluation.setText("0 / 5");
                    }
                });
            }
        }).run();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivitiesInteractionListener) {
            mListener = (OnActivitiesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnActivitiesInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnActivitiesInteractionListener {
        /**
         * Called when the activities in database have changed and UI should be updated.
         */
        void onActivitiesChanged();
    }
}
