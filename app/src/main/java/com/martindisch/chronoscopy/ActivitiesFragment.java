package com.martindisch.chronoscopy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ActivitiesFragment extends Fragment {

    public ActivitiesFragment() {
        // Required empty public constructor
    }

    public static ActivitiesFragment newInstance() {
        Log.e("FFF", "Recreating");
        return new ActivitiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

}
