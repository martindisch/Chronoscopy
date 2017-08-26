package com.martindisch.chronoscopy;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class NewActivityActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mSbRegret, mSbSkill, mSbFun;
    private EditText mEtDate, mEtTime;
    private TextView mTvRegret, mTvSkill, mTvFun;
    private TextInputLayout mTilDate, mTilTime;

    private static final String mDatePattern = "^(?:(?:(?:0[1-9]|1\\d|2[0-8])\\.(?:0[1-9]|1[0-2])|(?:29|30)\\.(?:0[13-9]|1[0-2])|31\\.(?:0[13578]|1[02]))\\.[1-9]\\d{3}|29\\.02\\.(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00))$";
    private static final String mTimePattern = "^\\d+:[0-5]\\d$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);

        // Prepare toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get views
        mSbRegret = (SeekBar) findViewById(R.id.newactivity_sbRegret);
        mSbSkill = (SeekBar) findViewById(R.id.newactivity_sbSkill);
        mSbFun = (SeekBar) findViewById(R.id.newactivity_sbFun);
        mTvRegret = (TextView) findViewById(R.id.newactivity_tvRegretStatus);
        mTvSkill = (TextView) findViewById(R.id.newactivity_tvSkillStatus);
        mTvFun = (TextView) findViewById(R.id.newactivity_tvFunStatus);
        mTilDate = (TextInputLayout) findViewById(R.id.newactivity_tilDate);
        mTilTime = (TextInputLayout) findViewById(R.id.newactivity_tilTime);
        mEtDate = (EditText) findViewById(R.id.newactivity_etDate);
        mEtTime = (EditText) findViewById(R.id.newactivity_etTime);

        // Set listeners
        mSbRegret.setOnSeekBarChangeListener(this);
        mSbSkill.setOnSeekBarChangeListener(this);
        mSbFun.setOnSeekBarChangeListener(this);
        mEtDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().matches(mDatePattern)) {
                    mTilDate.setError(null);
                } else {
                    mTilDate.setError(getString(R.string.error_date));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });
        mEtTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().matches(mTimePattern)) {
                    mTilTime.setError(null);
                } else {
                    mTilTime.setError(getString(R.string.error_time));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });
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
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

}
