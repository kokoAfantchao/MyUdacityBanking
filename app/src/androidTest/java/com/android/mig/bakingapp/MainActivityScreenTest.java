package com.android.mig.bakingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.android.mig.bakingapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    public static final String INGREDIENT_TEXT_SAMPLE = "unsalted butter, melted";
    private static final String STEP_TEXT_SAMPLE = "Prep the cookie crust.";
    private static final String RECIPE_TEXT_SAMPLE="Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void recipeHasCorrectText(){
        onView(withId(R.id.recipe_list_fragment))
                .check(matches(MyRecyclerViewMatcher
                        .atPositionOnView(0,withText(RECIPE_TEXT_SAMPLE),R.id.text_view_recipe_item)));
    }
    @Test
    public void clickRecyclerViewIngredientsButton_OpensIngredientActivity(){
        // clicks on button "Ingredients" within the RecyclerView item at position 0
        onView(withId(R.id.recipe_list_fragment))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.button_ingredients)));

        // checks if INGREDIENT_TEXT is found in RecyclerView(IngredientActivity) at position 1
        onView(withId(R.id.ingredient_list_fragment))
                .check(matches(MyRecyclerViewMatcher.atPositionOnView(1, withText(INGREDIENT_TEXT_SAMPLE), R.id.text_view_ingredient)));
    }

    @Test
    public void clickRecyclerViewStepsButton_OpensStepActivity(){
        // clicks on button "Steps" within the RecyclerView item at position 0 
        onView(withId(R.id.recipe_list_fragment))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.button_steps)));

        //checks if STEP_TEXT_SAMPLE is found in RecyclerView(StepActivity) at position 2
        onView(withId(R.id.step_list_fragment))
                .check(matches(MyRecyclerViewMatcher.atPositionOnView(2, withText(STEP_TEXT_SAMPLE), R.id.text_view_step)));
    }

    @Test
    public void recipeListHasNumberOfItems() {
        // checks if list of recipes contains 4 items
        onView(withId(R.id.recipe_list_fragment)).check(matches(MyRecyclerViewMatcher.hasNumberOfItems(4)));
    }


}