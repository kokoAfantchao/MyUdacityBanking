package com.android.mig.bakingapp.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.android.mig.bakingapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.text_view_servings), withText("Servings: "), isDisplayed()));
        appCompatTextView.perform(replaceText("Servings: 8"), closeSoftKeyboard());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.text_view_servings), withText("Servings: "), isDisplayed()));
        appCompatTextView2.perform(replaceText("Servings: 8"), closeSoftKeyboard());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.text_view_servings), withText("Servings: "), isDisplayed()));
        appCompatTextView3.perform(replaceText("Servings: 8"), closeSoftKeyboard());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.text_view_servings), withText("Servings: "), isDisplayed()));
        appCompatTextView4.perform(replaceText("Servings: 8"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_steps), withText("Steps"),
                        withParent(withId(R.id.linear_layout_recipe_buttons)),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.step_list_fragment),
                        withParent(allOf(withId(R.id.step_master_detail_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.exo_full_screen), withContentDescription("Full screen"), isDisplayed()));
        appCompatImageButton.perform(click());

    }

}
