package com.example.coursework.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.coursework.R;
import com.example.coursework.view.adapters.TabsAdapterSetGoals;

public class SetGoalsActivity extends AppCompatActivity {
    //region [properties]
    Toolbar toolbar;
    TabLayout tabLayout;
    TabsAdapterSetGoals tabsAdapter;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);

        //tabbed layout has it's own toolbar. default activity tool bar removed in manifest.
        //setup custom toolbar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);
        //adding required tabs
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"));
        tabLayout.addTab(tabLayout.newTab().setText("Seasonal"));
        tabLayout.addTab(tabLayout.newTab().setText("Annual"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.view_pager);
        tabsAdapter = new TabsAdapterSetGoals(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //setup callback for when tab selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //load the selected tab into  the display
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
    @Override
    protected void onNewIntent (Intent intent) {
        setIntent(intent);
    }
}
