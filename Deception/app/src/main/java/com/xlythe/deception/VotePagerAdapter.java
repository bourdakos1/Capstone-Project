package com.xlythe.deception;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Niko on 8/18/16.
 */

public class VotePagerAdapter extends SmartFragmentStatePagerAdapter {
    private static int NUM_ITEMS = 3;

    public VotePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new VoteFragment();
            case 1:
                return new VoteFragment();
            case 2:
                return new VoteFragment();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}