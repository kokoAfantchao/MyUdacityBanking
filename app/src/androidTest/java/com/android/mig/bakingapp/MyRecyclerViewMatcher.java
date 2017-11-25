package com.android.mig.bakingapp;

import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.mig.bakingapp.adapters.RecipesAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public class MyRecyclerViewMatcher {

    public static Matcher<View> atPositionOnView(final int position, final Matcher<View> itemMatcher,
                                                 @NonNull final int targetViewId) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has view id " + itemMatcher + " at position " + position);
            }

            @Override
            public boolean matchesSafely(final RecyclerView recyclerView) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                View targetView = viewHolder.itemView.findViewById(targetViewId);
                return itemMatcher.matches(targetView);
            }
        };
    }


    public static Matcher<View> hasNumberOfItems(final int numItems) {
        return new TypeSafeMatcher<View>() {

            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                RecipesAdapter recipesAdapter = (RecipesAdapter) recyclerView.getAdapter();
                return (recipesAdapter.getItemCount() == numItems);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has " + numItems + " items in RecyclerView");
            }
        };
    }
}
