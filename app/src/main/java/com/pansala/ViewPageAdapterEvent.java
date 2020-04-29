package com.pansala;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewPageAdapterEvent extends FragmentPagerAdapter {

   private final List<Fragment> fragmentList = new ArrayList<>();
   private final List<String> FragmentListTopics = new ArrayList<>();

    public ViewPageAdapterEvent(@NonNull FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return FragmentListTopics.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentListTopics.get(position);
    }

    public void AddNewFragment(Fragment fragment, String Title){
        fragmentList.add(fragment);
        FragmentListTopics.add(Title);
    }
}
