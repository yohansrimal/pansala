package com.pansala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class TempleManagement extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_management);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        viewPager = (ViewPager) findViewById(R.id.viewPagerTemple_id);
        ViewPagerAdapter_Temple adapter_temple = new ViewPagerAdapter_Temple(getSupportFragmentManager());

        //Adding Fragments

        adapter_temple.AddFragment(new Fragment_Temple_Details(), "පන්සලේ විස්තරය");
        adapter_temple.AddFragment(new Fragment_Temple_Contributions(), "පිංකම් එකතුව");
        adapter_temple.AddFragment(new Fragment_Temple_Privileges(), "පිංකම් දායකත්වය");

        //Adapter Setup
        viewPager.setAdapter(adapter_temple);
        tabLayout.setupWithViewPager(viewPager);

    }
}
