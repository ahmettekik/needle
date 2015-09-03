package com.example.android.needle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.needle.AllFragment;
import com.example.android.needle.Last15Fragment;

/**
 * Created by jonfisk on 02/09/15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new AllFragment();
            case 1:
                // Games fragment activity
                return new Last15Fragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
