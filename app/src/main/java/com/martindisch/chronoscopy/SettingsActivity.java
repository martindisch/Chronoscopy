package com.martindisch.chronoscopy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, TextWatcher {

    private SeekBar mSbRegret, mSbSkill, mSbFun, mSbResponsibility;
    private EditText mEtLeisure, mEtDate;
    private TextView mTvRegret, mTvSkill, mTvFun, mTvResponsibility;
    private TextInputLayout mTilDate;

    private static final String mDatePattern = "^(?:(?:(?:0[1-9]|1\\d|2[0-8])\\.(?:0[1-9]|1[0-2])|(?:29|30)\\.(?:0[13-9]|1[0-2])|31\\.(?:0[13578]|1[02]))\\.[1-9]\\d{3}|29\\.02\\.(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00))$";

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


        // Set listeners
        mSbRegret.setOnSeekBarChangeListener(this);
        mSbSkill.setOnSeekBarChangeListener(this);
        mSbFun.setOnSeekBarChangeListener(this);
        mSbResponsibility.setOnSeekBarChangeListener(this);
        mEtDate.addTextChangedListener(this);

        // Restore settings
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        mSbRegret.setProgress(prefs.getInt("regret", 5));
        mSbSkill.setProgress(prefs.getInt("skill", 5));
        mSbFun.setProgress(prefs.getInt("fun", 5));
        mSbResponsibility.setProgress(prefs.getInt("responsibility", 5));
        mEtLeisure.setText(prefs.getFloat("leisure", 31) + "");
        mEtDate.setText(prefs.getString("date", "01.01.1970"));
    }

    /**
     * Updates the responsible TextView for the SeekBar that changed.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.settings_sbRegret:
                mTvRegret.setText(String.valueOf(seekBar.getProgress()));
                break;
            case R.id.settings_sbSkill:
                mTvSkill.setText(String.valueOf(seekBar.getProgress()));
                break;
            case R.id.settings_sbFun:
                mTvFun.setText(String.valueOf(seekBar.getProgress()));
                break;
            case R.id.settings_sbResponsibility:
                mTvResponsibility.setText(String.valueOf(seekBar.getProgress()));
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
        editor.putInt("regret", mSbRegret.getProgress());
        editor.putInt("skill", mSbSkill.getProgress());
        editor.putInt("fun", mSbFun.getProgress());
        editor.putInt("responsibility", mSbResponsibility.getProgress());
        String sLeisure = mEtLeisure.getText().toString();
        if (sLeisure.length() > 0) {
            editor.putFloat("leisure", Float.parseFloat(sLeisure));
        }
        String sDate = mEtDate.getText().toString();
        if (sDate.matches(mDatePattern)) {
            editor.putString("date", sDate);
        }
        editor.apply();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
}
