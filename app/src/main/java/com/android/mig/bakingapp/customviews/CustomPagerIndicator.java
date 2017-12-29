package com.android.mig.bakingapp.customviews;

import android.support.v4.view.ViewPager;

/**
 * Created by nestorkokoafantchao on 8/29/17.
 */

public interface CustomPagerIndicator extends ViewPager.OnPageChangeListener {

    //To bind the indicator to the view pager
    void setViewPager(ViewPager viewPager);

    //To bind the indicator with default Position
    void setViewPager(ViewPager viewPager, int currentPosition);

    //Set the Pager to current position
    void setCurrentPosition(int position);


    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    //To notify any change of the fragment list
    void notifyDataChange();


}
