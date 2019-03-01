package com.diretta.livescore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.diretta.livescore.fragment.GuideLastFragment;
import com.diretta.livescore.fragment.GuideNormalFragment;

public class GuidePagerAdapter extends FragmentPagerAdapter {

    public GuidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                GuideNormalFragment normalFragment = new GuideNormalFragment();
                normalFragment.index = 0;
                return normalFragment;
            case 1:
                GuideNormalFragment normalFragment1 = new GuideNormalFragment();
                normalFragment1.index = 1;
                return normalFragment1;
            case 2:
                GuideNormalFragment normalFragment2 = new GuideNormalFragment();
                normalFragment2.index = 2;
                return normalFragment2;
            case 3:
                return new GuideLastFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}
