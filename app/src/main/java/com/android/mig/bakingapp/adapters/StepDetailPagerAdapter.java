package com.android.mig.bakingapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.mig.bakingapp.fragments.StepDetailFragment;
import com.android.mig.bakingapp.models.Step;

import java.util.ArrayList;

/**
 * Adapter for PageView in StepDetailActivity
 */

public class StepDetailPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Step> mStepArrayList;

    public StepDetailPagerAdapter(FragmentManager fm) {
        super(fm);
        mStepArrayList = new ArrayList<>();
    }

    public void setStepDetailAdapter(ArrayList<Step> stepArrayList){
        mStepArrayList = stepArrayList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return StepDetailFragment.ViewPagerSubFragment.newInstance(mStepArrayList.get(position), position);
    }

    @Override
    public int getCount() {
        if (mStepArrayList != null) {
            return mStepArrayList.size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mStepArrayList.get(position).getStepShortDescription();
    }

}
