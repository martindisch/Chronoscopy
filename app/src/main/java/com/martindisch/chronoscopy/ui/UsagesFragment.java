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
import android.widget.RatingBar;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrEvaluation;
import com.martindisch.chronoscopy.logic.ChrIndividual;
import com.martindisch.chronoscopy.logic.ChrUsage;
import com.martindisch.chronoscopy.logic.Util;

import java.util.List;

public class UsagesFragment extends Fragment {

    private RecyclerView mRvUsages;
    private UsageAdapter mAdapter;
    private List<ChrUsage> mUsages;
    private RatingBar mRbEvaluation;

    private OnUsagesInteractionListener mListener;

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
        mRbEvaluation = (RatingBar) view.findViewById(R.id.usages_rbToday);

        // Attempt reading today's rating from DB
        List<ChrEvaluation> evaluation = ChrEvaluation.find(
                ChrEvaluation.class, "date = ?", Util.getDate());
        if (evaluation.size() > 0) {
            // Restore rating
            mRbEvaluation.setRating(evaluation.get(0).getRating());
        }
        mRbEvaluation.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                String date = Util.getDate();
                // Build new evaluation
                ChrEvaluation evaluation = new ChrEvaluation((int) v, date);
                // Attempt reading today's previous rating from DB
                List<ChrEvaluation> eval = ChrEvaluation.find(
                        ChrEvaluation.class, "date = ?", date);
                if (eval.size() > 0) {
                    // Previous evaluation exists, update it
                    evaluation.update();
                } else {
                    // First evaluation for today, save it
                    evaluation.save();
                }
                mListener.onUsagesChanged();
            }
        });

        // Prepare RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvUsages.setLayoutManager(layoutManager);
        mRvUsages.addItemDecoration(new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation()));
        mRvUsages.setHasFixedSize(true);
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
                // Get usage that was swiped away
                ChrUsage toDelete = mUsages.get(position);
                // Delete the usage from DB
                ChrUsage.delete(toDelete);
                // Remove the usage from the list used by the adapter
                mUsages.remove(position);
                // Notify adapter of removal
                mRvUsages.getAdapter().notifyItemRemoved(position);
                // Notify MainActivity of data change
                mListener.onUsagesChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(mRvUsages);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Reload usages from DB and update RecyclerView.
     */
    public void updateUI() {
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRvUsages.setAdapter(mAdapter);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUsagesInteractionListener) {
            mListener = (OnUsagesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUsagesInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUsagesInteractionListener {
        /**
         * Called when the usages in database have changed and UI should be updated.
         */
        void onUsagesChanged();
    }
}
