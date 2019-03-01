package com.diretta.livescore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.diretta.livescore.fragment.DiscoveryFragment;
import com.diretta.livescore.fragment.GuideLastFragment;
import com.diretta.livescore.fragment.GuideNormalFragment;

public class DiscoveryPagerAdapter extends FragmentStatePagerAdapter {

    public DiscoveryPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    /*
    @Override
    public float getPageWidth(int position) {
        return 0.93f;
    }
*/
    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                DiscoveryFragment normalFragment = new DiscoveryFragment();
                normalFragment.index = 0;
                return normalFragment;
            case 1:
                DiscoveryFragment normalFragment1 = new DiscoveryFragment();
                normalFragment1.index = 1;
                return normalFragment1;
            case 2:
                DiscoveryFragment normalFragment2 = new DiscoveryFragment();
                normalFragment2.index = 2;
                return normalFragment2;
            case 3:
                DiscoveryFragment normalFragment3 = new DiscoveryFragment();
                normalFragment3.index = 3;
                return normalFragment3;
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}
