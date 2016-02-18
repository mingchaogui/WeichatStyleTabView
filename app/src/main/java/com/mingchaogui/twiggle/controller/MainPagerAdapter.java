package com.mingchaogui.twiggle.controller;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {

    Fragment[] mFragmentArr = new Fragment[3];

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragmentArr[position];
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new TweetFragment();
                    break;

                case 1:
                    fragment = new FocusFragment();
                    break;

                case 2:
                    fragment = new SelfFragment();
                    break;
            }
            mFragmentArr[position] = fragment;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentArr.length;
    }
}
