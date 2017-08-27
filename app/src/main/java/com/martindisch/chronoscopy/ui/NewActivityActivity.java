package com.martindisch.chronoscopy.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.martindisch.chronoscopy.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewActivityActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mSbRegret, mSbSkill, mSbFun;
    private EditText mEtName, mEtDate, mEtTime;
    private TextView mTvRegret, mTvSkill, mTvFun;
    private TextInputLayout mTilDate, mTilTime;

    private static final String mDatePattern = "^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)$";
    private static final String mTimePattern = "^(\\d+:)?[0-5]?\\d$";

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
        mEtName = (EditText) findViewById(R.id.newactivity_etName);
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

        // Set input
        mEtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    /**
     * Returns if both EditTexts match their respective regex and a name is filled in.
     *
     * @return true if inputs are valid
     */
    private boolean isInputValid() {
        String dateString = mEtDate.getText().toString();
        String timeString = mEtTime.getText().toString();
        String nameString = mEtName.getText().toString();
        return dateString.matches(mDatePattern) &&
                timeString.matches(mTimePattern) &&
                nameString.length() > 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if (isInputValid()) {
                // TODO: check if activity exists already
                // TODO: otherwise, save usage
                finish();
            } else {
                Snackbar.make(mTvFun, R.string.invalid_inputs, Snackbar.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the responsible TextView for the SeekBar that changed.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.newactivity_sbRegret:
                mTvRegret.setText(String.valueOf((seekBar.getProgress() + 10) / 10.0));
                break;
            case R.id.newactivity_sbSkill:
                mTvSkill.setText(String.valueOf((seekBar.getProgress() + 10) / 10.0));
                break;
            case R.id.newactivity_sbFun:
                mTvFun.setText(String.valueOf((seekBar.getProgress() + 10) / 10.0));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

}
