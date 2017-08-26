package com.android.mig.bakingapp;


import android.support.annotation.IdRes;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.activities.MainActivity;
import com.android.mig.bakingapp.activities.StepActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class StepActivityBasicTest {

    @Rule
    public ActivityTestRule<StepActivity> mActivityTestRule = new ActivityTestRule<>(StepActivity.class);

    @Test
    public void stepActivityBasicTest() {

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.text_view_step_description), withText("Recipe Introduction"),
                        childAtPosition(
                                withParent(withId(R.id.viewpager_step_detail)),
                                1),
                        isDisplayed()));
      ImageButton imageButton=(ImageButton) mActivityTestRule.getActivity().findViewById(R.id.exo_full_screen);


        ViewInteraction viewInteraction = onView(withId(R.id.step_detail_container));



//        ViewInteraction appCompatImageButton = onView(
//                allOf(withId(R.id.exo_full_screen), withContentDescription("Full screen"), isDisplayed()));
//        appCompatImageButton.perform(click());


    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);

            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }




}
