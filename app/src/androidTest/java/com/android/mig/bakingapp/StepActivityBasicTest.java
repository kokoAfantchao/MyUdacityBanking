package com.android.mig.bakingapp;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.android.mig.bakingapp.activities.StepActivity;
import com.android.mig.bakingapp.models.Step;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class StepActivityBasicTest {

    @Rule
    public ActivityTestRule<StepActivity> mActivityTestRule = new ActivityTestRule<>(StepActivity.class);
    private static List<Step> fakeSteps = new ArrayList<>();

    @Before
    public void stubAllExternalIntents() {
        intending(not(IntentMatchers.isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Before
    public void init() {
        for (int i = 0; i < 10; i++) {
            fakeSteps.add(new Step(i + 1,
                    "Starting prep " + i,
                    "1. Preheat the oven to 350°F. Butter a 9\" deep dish pie pan.",
                    "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                    ""));
        }
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
    }


    @Test
    public void stepActivityBasicTest() {
        ViewInteraction textView3 = onView(allOf(withId(R.id.text_view_step_description), withText("Recipe Introduction"),
                childAtPosition(
                        withParent(withId(R.id.viewpager_step_detail)),
                        1),
                isDisplayed()));
        ViewInteraction viewInteraction = onView(withId(R.id.step_detail_container));
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
