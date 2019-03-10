package com.example.coursework.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.coursework.R;
import com.example.coursework.view.adapters.TabsAdapterCheckGoals;
import com.example.coursework.view.adapters.TabsAdapterSetGoals;

public class CheckGoalsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    TabsAdapterCheckGoals tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_goals);

        //tabbed layout has it's own toolbar. default activity tool bar removed in manifest
        toolbar = findViewById(R.id.tool_bar_check);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout_check);
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"));
        tabLayout.addTab(tabLayout.newTab().setText("Seasonal"));
        tabLayout.addTab(tabLayout.newTab().setText("Annual"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.view_pager_check);
        tabsAdapter = new TabsAdapterCheckGoals(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
