package com.nguyenvukhanhuygmail.shoppingonline.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nguyenvukhanhuygmail.shoppingonline.fragment.ProfileFragment;
import com.nguyenvukhanhuygmail.shoppingonline.fragment.ShoppingFragment;

/**
 * Created by toannq on 1/13/2018.
 */

public class Tabbar_Adapter  extends FragmentStatePagerAdapter{

    private int mNumOfTabs;

    public Tabbar_Adapter(FragmentManager fm, int NumOfTabs) {

        super(fm);
        this.mNumOfTabs = NumOfTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ShoppingFragment();
            case 1:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
