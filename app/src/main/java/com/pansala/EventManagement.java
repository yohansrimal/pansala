package com.pansala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class EventManagement extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__management);
        tabLayout = (TabLayout) findViewById(R.id.eventTab_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.eventBar_id);
        viewPager = (ViewPager) findViewById(R.id.viewParentEvent_id);

        ViewPageAdapterEvent adapterEvent = new ViewPageAdapterEvent(getSupportFragmentManager());
        //Adding newly created Fragments
        adapterEvent.AddNewFragment(new FragmentAddEvent(),"නව අවස්ථාවන්");
        adapterEvent.AddNewFragment(new FragmentEventList(),"අවස්ථා එකතුව");
        adapterEvent.AddNewFragment(new FragmentEventManage(),"කළමනාකරණය");

        //Adapting the setup

        viewPager.setAdapter(adapterEvent);
        tabLayout.setupWithViewPager(viewPager);


    }
}
