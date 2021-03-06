package com.example.coursework.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.coursework.view.fragments.SetAnnualGoal;
import com.example.coursework.view.fragments.SetSeasonalGoal;
import com.example.coursework.view.fragments.SetWeeklyGoal;
//TabsAdapterCheckGoals: an adapter to decide what fragment to load when user uses the tabbed navigation in  SetGoals activity
public class TabsAdapterSetGoals extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    public TabsAdapterSetGoals(FragmentManager fm, int NoOfTabs){
        super(fm);
        this.mNumOfTabs = NoOfTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        //switch to decide what fragment to load based on selected tab index
        switch (position){
            case 0:
                return new SetWeeklyGoal();
            case 1:
                return new SetSeasonalGoal();
            case 2:
                return new SetAnnualGoal();
            default:
                return null;
        }
    }
}
