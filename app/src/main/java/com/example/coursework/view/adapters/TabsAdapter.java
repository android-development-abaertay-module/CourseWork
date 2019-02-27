package com.example.coursework.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.coursework.view.fragments.SetAnnualGoal;
import com.example.coursework.view.fragments.SetSeasonalGoal;
import com.example.coursework.view.fragments.SetWeeklyGoal;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                SetWeeklyGoal weeklyGoal = new SetWeeklyGoal();
                return weeklyGoal;
            case 1:
                SetSeasonalGoal seasonalGoal = new SetSeasonalGoal();
                return seasonalGoal;
            case 2:
                SetAnnualGoal annualGoal = new SetAnnualGoal();
                return annualGoal;
            default:
                return null;
        }
    }
}
