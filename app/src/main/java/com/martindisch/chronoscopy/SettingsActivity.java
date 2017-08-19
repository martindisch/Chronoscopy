package com.martindisch.chronoscopy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mSbRegret, mSbSkill, mSbFun, mSbResponsibility;
    private EditText mEtLeisure, mEtDob;
    private TextView mTvRegret, mTvSkill, mTvFun, mTvResponsibility;

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
        mEtDob = (EditText) findViewById(R.id.settings_etDate);

        // Set listeners
        mSbRegret.setOnSeekBarChangeListener(this);
        mSbSkill.setOnSeekBarChangeListener(this);
        mSbFun.setOnSeekBarChangeListener(this);
        mSbResponsibility.setOnSeekBarChangeListener(this);
    }

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

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
