package com.android.mig.bakingapp.customviews;

import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.mig.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nestorkokoafantchao on 8/29/17.
 */

public class NextPreviousIndicator extends LinearLayout implements CustomPagerIndicator {

   @BindView(R.id.button_next)
   Button  mNextButton;
   @BindView(R.id.button_previous)
   Button  previousButton;
   private ViewPager mViewPager;
   private ViewPager.OnPageChangeListener mOnPageChangeListener;
   private  int mCurrentePage;
   private  int mTotalPages;

    public NextPreviousIndicator(Context context, AttributeSet attributeSet){
     super(context,attributeSet);

     LayoutInflater layoutInflater =  LayoutInflater.from(context);
     View  rootView = layoutInflater.inflate(R.layout.nexty_previous_indicator, this, true);
     ButterKnife.bind(rootView);

     mNextButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            if((mCurrentePage+1<=mTotalPages)) {
             setCurrentPosition(mCurrentePage+1);
            }
         }
     });
     previousButton.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
             if(mCurrentePage-1>0){
                 setCurrentPosition(mCurrentePage - 1);
             }
          }
         });
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
       if(mViewPager== viewPager) {
           return;
       }
       if(mViewPager!= null){
           mViewPager.clearOnPageChangeListeners();
       }
       if(viewPager.getAdapter()== null ){
           throw new   IllegalStateException("ViewPager does not have adapter instance.");
       }
       mViewPager= viewPager;
       mViewPager.addOnPageChangeListener(this);
       mTotalPages=viewPager.getAdapter().getCount();
       invalidate();

    }

    @Override
    public void setViewPager(ViewPager viewPager, int currentPosition) {
       setViewPager( viewPager);
       setCurrentPosition(currentPosition);
    }

    @Override
    public void setCurrentPosition(int position) {
       if(mViewPager == null ){
          throw  new IllegalStateException("ViewPager has not  been bound ");
       }
       mViewPager.setCurrentItem(position,true);
       mCurrentePage =position;
       invalidate();

    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    @Override
    public void notifyDataChange() {
     invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mOnPageChangeListener!= null){
            mOnPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentPosition(position);
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(mOnPageChangeListener!= null ){
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }

    }
}
