package com.android.mig.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.android.mig.bakingapp.activities.MainActivity;

import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by nestorkokoafantchao on 11/6/17.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<MainActivity>(MainActivity.class);
    private static String INTENT_PACKAGE = "com.android.mig.bakingapp";

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void StepsItendingTest() {
        String string = intentsTestRule.getActivity().getResources().getString(R.string.action_steps);
        Espresso.onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(3, MyViewAction.clickChildViewWithId(R.id.button_steps)));
        intended(AllOf.allOf(hasExtraWithKey(string), toPackage(INTENT_PACKAGE)));
    }

    @Test
    public void IngrediensItendingTest() {
        String string = intentsTestRule.getActivity().getResources().getString(R.string.action_ingredients);
        Espresso.onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(3, MyViewAction.clickChildViewWithId(R.id.button_ingredients)));
        intended(AllOf.allOf(hasExtraWithKey(string), toPackage(INTENT_PACKAGE)));
    }

}
