package com.martindisch.chronoscopy.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrActivity;
import com.martindisch.chronoscopy.logic.ChrEvaluation;
import com.martindisch.chronoscopy.logic.ChrUsage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener, TextWatcher, View.OnClickListener {

    private SeekBar mSbRegret, mSbSkill, mSbFun, mSbResponsibility;
    private EditText mEtLeisure, mEtDate;
    private TextView mTvRegret, mTvSkill, mTvFun, mTvResponsibility;
    private TextInputLayout mTilDate;
    private Button mExport;

    private static final String mDatePattern = "^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Prepare toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get views
        mSbRegret = (SeekBar) findViewById(R.id.settings_sbRegret);
        mSbSkill = (SeekBar) findViewById(R.id.settings_sbSkill);
        mSbFun = (SeekBar) findViewById(R.id.settings_sbFun);
        mSbResponsibility = (SeekBar) findViewById(R.id.settings_sbResponsibility);
        mTvRegret = (TextView) findViewById(R.id.settings_tvRegretStatus);
        mTvSkill = (TextView) findViewById(R.id.settings_tvSkillStatus);
        mTvFun = (TextView) findViewById(R.id.settings_tvFunStatus);
        mTvResponsibility = (TextView) findViewById(R.id.settings_tvResponsibilityStatus);
        mEtLeisure = (EditText) findViewById(R.id.settings_etLeisure);
        mEtDate = (EditText) findViewById(R.id.settings_etDate);
        mTilDate = (TextInputLayout) findViewById(R.id.settings_tilDate);
        mExport = (Button) findViewById(R.id.settings_bExport);

        // Set listeners
        mSbRegret.setOnSeekBarChangeListener(this);
        mSbSkill.setOnSeekBarChangeListener(this);
        mSbFun.setOnSeekBarChangeListener(this);
        mSbResponsibility.setOnSeekBarChangeListener(this);
        mEtDate.addTextChangedListener(this);
        mExport.setOnClickListener(this);

        // Restore settings
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        mSbRegret.setProgress(prefs.getInt("regret", 6) - 1);
        mSbSkill.setProgress(prefs.getInt("skill", 6) - 1);
        mSbFun.setProgress(prefs.getInt("fun", 6) - 1);
        mSbResponsibility.setProgress(prefs.getInt("responsibility", 6) - 1);
        mEtLeisure.setText(prefs.getFloat("leisure", 31) + "");
        mEtDate.setText(prefs.getString("date", "1970-01-01"));
    }

    /**
     * Updates the responsible TextView for the SeekBar that changed.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.settings_sbRegret:
                mTvRegret.setText(String.valueOf(seekBar.getProgress() + 1));
                break;
            case R.id.settings_sbSkill:
                mTvSkill.setText(String.valueOf(seekBar.getProgress() + 1));
                break;
            case R.id.settings_sbFun:
                mTvFun.setText(String.valueOf(seekBar.getProgress() + 1));
                break;
            case R.id.settings_sbResponsibility:
                mTvResponsibility.setText(String.valueOf(seekBar.getProgress() + 1));
                break;
        }
    }

    /**
     * Displays whether the date input is valid or not.
     */
    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().matches(mDatePattern)) {
            mTilDate.setError(null);
        } else {
            mTilDate.setError(getString(R.string.error_date));
        }
    }

    /**
     * Save settings.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("regret", mSbRegret.getProgress() + 1);
        editor.putInt("skill", mSbSkill.getProgress() + 1);
        editor.putInt("fun", mSbFun.getProgress() + 1);
        editor.putInt("responsibility", mSbResponsibility.getProgress() + 1);
        String sLeisure = mEtLeisure.getText().toString();
        if (sLeisure.length() > 0) {
            editor.putFloat("leisure", Float.parseFloat(sLeisure));
        }
        String sDate = mEtDate.getText().toString();
        if (sDate.matches(mDatePattern)) {
            editor.putString("date", sDate);
        }
        editor.commit();
    }

    /**
     * Export data.
     */
    @Override
    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject activities = new JSONObject();
                    JSONArray usages = new JSONArray();
                    // Go through all activities
                    for (ChrActivity activity : ChrActivity.listAll(ChrActivity.class)) {
                        // Collect properties of the activity
                        JSONObject currentActivity = new JSONObject();
                        currentActivity.put("regret", activity.getRegret());
                        currentActivity.put("skill", activity.getSkill());
                        currentActivity.put("fun", activity.getFun());
                        // Add the activity to activities
                        activities.put(activity.getName(), currentActivity);

                        // Go through all usages of the activity
                        for (ChrUsage usage : ChrUsage.find(
                                ChrUsage.class, "activity_id = ?", activity.getId() + "")) {
                            // Collect properties of the usage
                            JSONObject currentUsage = new JSONObject();
                            currentUsage.put("activity", activity.getName());
                            currentUsage.put("date", usage.getDate());
                            currentUsage.put("time", usage.getTime());
                            // Add the usage to usages
                            usages.put(currentUsage);
                        }
                    }

                    JSONObject evaluations = new JSONObject();
                    // Go through all evaluations
                    for (ChrEvaluation evaluation : ChrEvaluation.listAll(ChrEvaluation.class)) {
                        // Add the evaluation to evaluations
                        evaluations.put(evaluation.getDate(), evaluation.getRating());
                    }

                    // Build collection of all data
                    JSONObject data = new JSONObject();
                    data.put("activities", activities);
                    data.put("usages", usages);
                    data.put("evaluations", evaluations);
                    Log.e("FFF", data.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
}
