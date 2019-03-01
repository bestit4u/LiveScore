package com.diretta.livescore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.diretta.livescore.fragment.GuideLastFragment;
import com.diretta.livescore.fragment.GuideNormalFragment;
import com.diretta.livescore.fragment.PremiumGuide1Fragment;
import com.diretta.livescore.fragment.PremiumGuide2Fragment;
import com.diretta.livescore.fragment.PremiumGuide3Fragment;
import com.diretta.livescore.fragment.PremiumGuide4Fragment;
import com.diretta.livescore.fragment.PremiumGuide5Fragment;
import com.diretta.livescore.fragment.PremiumGuide6Fragment;

public class PremiumGuidePagerAdapter extends FragmentPagerAdapter {

    public PremiumGuidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new PremiumGuide1Fragment();
            case 1:
                return new PremiumGuide2Fragment();
            case 2:
                return new PremiumGuide3Fragment();
            case 3:
                return new PremiumGuide5Fragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}
