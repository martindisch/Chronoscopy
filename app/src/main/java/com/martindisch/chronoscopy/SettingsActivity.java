package com.martindisch.chronoscopy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mSbRegret, mSbSkill, mSbFun, mSbResponsibility;
    private EditText mEtRegret, mEtSkill, mEtFun, mEtResponsibility, mEtLeisure, mEtDob;

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
        mEtRegret = (EditText) findViewById(R.id.settings_etRegret);
        mEtSkill = (EditText) findViewById(R.id.settings_etSkill);
        mEtFun = (EditText) findViewById(R.id.settings_etFun);
        mEtResponsibility = (EditText) findViewById(R.id.settings_etResponsibility);
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
                mEtRegret.setText(String.valueOf(seekBar.getProgress()));
                break;
            case R.id.settings_sbSkill:
                mEtSkill.setText(String.valueOf(seekBar.getProgress()));
                break;
            case R.id.settings_sbFun:
                mEtFun.setText(String.valueOf(seekBar.getProgress()));
                break;
            case R.id.settings_sbResponsibility:
                mEtResponsibility.setText(String.valueOf(seekBar.getProgress()));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
