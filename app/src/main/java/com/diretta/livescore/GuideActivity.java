package com.diretta.livescore;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.diretta.livescore.adapter.GuidePagerAdapter;

/**
 * Created by suc on 4/25/2018.
 */

public class GuideActivity extends FragmentActivity {
    ViewPager viewPager;
    GuidePagerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        mAdapter = new GuidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
    }
}
