package com.example.coursework.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.coursework.view.fragments.CheckAnnualGoal;
import com.example.coursework.view.fragments.CheckSeasonalGoal;
import com.example.coursework.view.fragments.CheckWeeklyGoal;

public class TabsAdapterCheckGoals extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public TabsAdapterCheckGoals(FragmentManager fm, int NoOfTabs){
        super(fm);
        this.mNumOfTabs = NoOfTabs;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new CheckWeeklyGoal();
            case 1:
                return new CheckSeasonalGoal();
            case 2:
                return new CheckAnnualGoal();
            default:
                return null;
        }
    }
}
