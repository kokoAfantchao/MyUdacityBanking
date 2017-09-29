package com.android.mig.bakingapp.activities;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.android.mig.bakingapp.R;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {
    private static final int ITEM_BELOW_THE_FOLD = 3;
    @Rule
    public  ActivityTestRule<StepActivity> mStepActivityActivityTestRule=
            new ActivityTestRule<>(StepActivity.class);

    @Before
    public void  init (){
       mStepActivityActivityTestRule.getActivity().getSupportFragmentManager()
         .beginTransaction();
    }

    @Test
    public void mainActivityTest2() {
        onView(withId(R.id.steps_recycler_view)).
                perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD,click()));
    }

}
