package com.android.mig.bakingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.android.mig.bakingapp.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    public static final String INGREDIENT_TEXT_SAMPLE = "unsalted butter, melted";
    private static final String STEP_TEXT_SAMPLE = "Combine dry ingredients";
    private static final String RECIPE_TEXT_SAMPLE="Nutella Pie";
    private static  String buttonIngredients  ;
    private static  String buttonSteps  ;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recipeHasCorrectText(){
        buttonIngredients= mActivityTestRule.getActivity().getResources()
                .getString(R.string.action_ingredients);
        buttonSteps = mActivityTestRule.getActivity().getResources()
                .getString(R.string.action_steps);
        ViewInteraction viewInteraction = onView(withId(R.id.recipes_recycler_view));
                        viewInteraction.check(matches(MyRecyclerViewMatcher.atPositionOnView(
                                         0,withText(RECIPE_TEXT_SAMPLE),R.id.text_view_recipe_item)));
                        viewInteraction.check(matches(MyRecyclerViewMatcher.atPositionOnView(
                                         0,withText(buttonIngredients),R.id.button_ingredients)));
                        viewInteraction.check(matches(MyRecyclerViewMatcher.atPositionOnView(
                                         0,withText(buttonSteps),R.id.button_steps)));
    }
    @Test
    public void clickRecyclerViewIngredientsButton_OpensIngredientActivity(){
        // clicks on button "Ingredients" within the RecyclerView item at position 0
        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.button_ingredients)));
        // checks if INGREDIENT_TEXT is found in RecyclerView(IngredientActivity) at position 1
        onView(withId(R.id.ingredient_list_fragment))
                .check(matches(MyRecyclerViewMatcher.atPositionOnView(1, withText(INGREDIENT_TEXT_SAMPLE), R.id.text_view_ingredient)));
    }

    @Test
    public void clickRecyclerViewStepsButton_OpensStepActivity(){
        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(3,MyViewAction.clickChildViewWithId(R.id.button_steps)));
    }




}