package com.martindisch.chronoscopy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.martindisch.chronoscopy.R;
import com.orm.SugarContext;

public class MainActivity extends AppCompatActivity implements
        UsagesFragment.OnUsagesInteractionListener,
        ActivitiesFragment.OnActivitiesInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ActivitiesFragment mActivitiesFragment;
    private UsagesFragment mUsagesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SugarORM connection
        SugarContext.init(this);

        // Prepare toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the fragment adapter
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        // Set icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_activities);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_history);
        // Set tab indicator color to white because the accent color wouldn't stand out enough
        tabLayout.setSelectedTabIndicatorColor(
                ContextCompat.getColor(this, android.R.color.white)
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewActivityActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Terminate SugarORM connection
        SugarContext.terminate();
    }

    @Override
    public void onUsagesChanged() {
        mActivitiesFragment.updateUI();
    }

    @Override
    public void onActivitiesChanged() {
        mUsagesFragment.updateUI();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                mActivitiesFragment = ActivitiesFragment.newInstance();
                return mActivitiesFragment;
            } else {
                mUsagesFragment = UsagesFragment.newInstance();
                return mUsagesFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
