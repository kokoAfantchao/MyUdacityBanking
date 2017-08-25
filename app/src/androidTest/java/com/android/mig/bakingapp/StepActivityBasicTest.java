package com.android.mig.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.android.mig.bakingapp.activities.StepActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

/**
 * Created by nestorkokoafantchao on 8/18/17.
 */


@RunWith(AndroidJUnit4.class)
public class StepActivityBasicTest {

    private  final  static  String TEXT_RECIP_INTRO="recipe Introduction";

    @Rule
    public ActivityTestRule<StepActivity>  activityTestRule = new ActivityTestRule<>(StepActivity.class);

    @Test
    public void clickOnStepItemList(){
        onView(ViewMatchers.withId(R.id.step_list_fragment))
                .perform(RecyclerViewActions.scrollToPosition(0),ViewActions.click());
        onView(ViewMatchers.withId(R.id.text_view_step_description))
                .check(ViewAssertions.matches(ViewMatchers.withText(TEXT_RECIP_INTRO)));
    }
}
