package com.martindisch.chronoscopy.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.martindisch.chronoscopy.R;
import com.martindisch.chronoscopy.logic.ChrActivity;
import com.martindisch.chronoscopy.logic.ChrEvaluation;
import com.martindisch.chronoscopy.logic.ChrIndividual;
import com.martindisch.chronoscopy.logic.ChrUsage;
import com.martindisch.chronoscopy.logic.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
                    // Get individual for calculation of scores
                    ChrIndividual individual = Util.getIndividual(getApplicationContext());

                    JSONObject jsonActivities = new JSONObject();
                    JSONArray jsonUsages = new JSONArray();
                    // Go through all activities
                    for (ChrActivity activity : ChrActivity.listAll(ChrActivity.class)) {
                        // Collect properties of the activity
                        JSONObject jsonActivity = new JSONObject();
                        jsonActivity.put("regret", activity.getRegret());
                        jsonActivity.put("skill", activity.getSkill());
                        jsonActivity.put("fun", activity.getFun());
                        jsonActivity.put("score_per_hour",
                                individual.getScorePerHour(activity));
                        // Add the activity to activities
                        jsonActivities.put(activity.getName(), jsonActivity);

                        // Go through all usages of the activity
                        for (ChrUsage usage : ChrUsage.find(
                                ChrUsage.class, "activity_id = ?", activity.getId() + "")) {
                            // Collect properties of the usage
                            JSONObject jsonUsage = new JSONObject();
                            jsonUsage.put("activity", activity.getName());
                            jsonUsage.put("date", usage.getDate());
                            jsonUsage.put("time", usage.getTime());
                            jsonUsage.put("score",
                                    individual.getScore(activity, usage.getTimeHours()));
                            // Add the usage to usages
                            jsonUsages.put(jsonUsage);
                        }
                    }

                    JSONObject jsonEvaluations = new JSONObject();
                    // Go through all evaluations
                    for (ChrEvaluation evaluation : ChrEvaluation.listAll(ChrEvaluation.class)) {
                        // Add the evaluation to evaluations
                        jsonEvaluations.put(evaluation.getDate(), evaluation.getRating());
                    }

                    // Build individual
                    JSONObject jsonIndividual = new JSONObject();
                    jsonIndividual.put("age", individual.getAge());
                    jsonIndividual.put("leisure", individual.getLeisure());
                    jsonIndividual.put("responsibility", individual.getResponsibility());
                    jsonIndividual.put("regret", individual.getRegret());
                    jsonIndividual.put("skill", individual.getSkill());
                    jsonIndividual.put("fun", individual.getFun());
                    jsonIndividual.put("qTime", individual.getTimeValue());

                    // Build collection of all data
                    JSONObject data = new JSONObject();
                    data.put("activities", jsonActivities);
                    data.put("usages", jsonUsages);
                    data.put("evaluations", jsonEvaluations);
                    data.put("individual", jsonIndividual);

                    // Create and write output file in cache directory
                    File outFile = new File(
                            getApplicationContext().getCacheDir(), Util.getDate() + ".json");
                    OutputStreamWriter writer = new OutputStreamWriter(
                            new FileOutputStream(outFile));
                    writer.write(data.toString());
                    writer.close();

                    // Get URI from FileProvider
                    Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                            "com.martindisch.chronoscopy.fileprovider", outFile);

                    // Create sharing intent
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    // Temporary permission for receiving app to read this file
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setType("application/json");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    Snackbar.make(mExport, R.string.error_export, Snackbar.LENGTH_LONG).show();
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
