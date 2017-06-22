package com.android.mig.bakingapp.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.activities.IngredientActivity;
import com.android.mig.bakingapp.activities.StepActivity;
import com.android.mig.bakingapp.models.Ingredient;
import com.android.mig.bakingapp.models.Recipe;
import com.android.mig.bakingapp.models.Step;
import com.android.mig.bakingapp.adapters.RecipesAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.android.mig.bakingapp.utils.OpenRecipesJsonUtils.*;

public class RecipeListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>{

    private static final int LOADER_ID = 900;
    final private String RECIPES_ADDRESS = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private URL recipeURL = null;
    public boolean isTablet;                // used to help distinguish between tablet and handset

    RecipesAdapter mRecipesAdapter;
    View rootView;

    public RecipeListFragment(){
        try {
            recipeURL = new URL(RECIPES_ADDRESS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        RecyclerView recipesRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipes_recycler_view);

        // sets the number of columns in recipe list according to the screen configuration
        int orientation = getResources().getConfiguration().orientation;
        int smallestWidth = getResources().getConfiguration().smallestScreenWidthDp;
        // if it's a handset
        if (smallestWidth < getResources().getInteger(R.integer.sw600dp)){
            isTablet = false;
            if (orientation == Configuration.ORIENTATION_PORTRAIT){
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
                recipesRecyclerView.setLayoutManager(linearLayoutManager);
            } else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        getResources().getInteger(R.integer.landscape_handset_recipes_list_num_columns),
                        LinearLayoutManager.VERTICAL,
                        false);
                recipesRecyclerView.setLayoutManager(gridLayoutManager);
            }
        // if it's a tablet
        } else {
            isTablet = true;
            int numColumns;
            if (orientation == Configuration.ORIENTATION_PORTRAIT){
                numColumns = getResources().getInteger(R.integer.portrait_tablet_recipes_list_num_columns);
            } else {
                numColumns = getResources().getInteger(R.integer.landscape_tablet_recipes_list_num_columns);
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), numColumns, LinearLayoutManager.VERTICAL, false);
            recipesRecyclerView.setLayoutManager(gridLayoutManager);
        }

        mRecipesAdapter = new RecipesAdapter(new RecipesAdapter.OnClickHandler() {
            @Override
            public void OnClickIngredient(String recipe, ArrayList<Ingredient> ingredients) {
                Intent intent = new Intent(getActivity(), IngredientActivity.class);
                intent.putExtra(String.valueOf(R.string.action_ingredients), ingredients);
                intent.putExtra(Intent.EXTRA_TEXT, recipe);
                startActivity(intent);
            }

            @Override
            public void OnClickStep(ArrayList<Step> steps) {
                Intent intent = new Intent(getActivity(), StepActivity.class);
                intent.putExtra(String.valueOf(R.string.action_steps), steps);
                intent.putExtra(Intent.ACTION_CONFIGURATION_CHANGED, isTablet);
                startActivity(intent);
            }
        });
        recipesRecyclerView.setAdapter(mRecipesAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(rootView.getContext()) {

            ArrayList<Recipe> recipesArray = null;

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {

                ArrayList<Recipe> resultArray = null;
                // retrieves recipes from the json url provided and passes to the adapter
                try {
                    String jsonResult = getResponseFromHttpUrl(recipeURL);
                    resultArray = getRecipeArrayFromJson(jsonResult);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return resultArray;
            }

            @Override
            public void deliverResult(ArrayList<Recipe> data) {
                recipesArray = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        if (data != null){
            mRecipesAdapter.setRecipesAdapter(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }
}
