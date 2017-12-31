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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.activities.IngredientActivity;
import com.android.mig.bakingapp.activities.StepActivity;
import com.android.mig.bakingapp.adapters.RecipesAdapter;
import com.android.mig.bakingapp.models.Ingredient;
import com.android.mig.bakingapp.models.Recipe;
import com.android.mig.bakingapp.models.Step;
import com.android.mig.bakingapp.services.RecipesService;
import com.android.mig.bakingapp.utils.AppConnectivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

    private static final int LOADER_ID = 900;
    private static final String TAG = RecipeListFragment.class.toString();
    public boolean isTablet;        // used to help distinguish between tablet and handset
    RecipesAdapter mRecipesAdapter;
    @BindView(R.id.recipes_recycler_view)
    RecyclerView recipesRecyclerView;
    @BindView(R.id.empty_list_view)
    LinearLayout linearLayoutEmptyView;
    @BindView(R.id.button_reload)
    ImageView imageButtonReload;


    public RecipeListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        // sets the number of columns in recipe list according to the screen configuration
        int orientation = getResources().getConfiguration().orientation;
        int smallestWidth = getResources().getConfiguration().smallestScreenWidthDp;
        // if it's a handset
        if (smallestWidth < getResources().getInteger(R.integer.sw600dp)) {
            isTablet = false;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
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
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                numColumns = getResources().getInteger(R.integer.portrait_tablet_recipes_list_num_columns);
            } else {
                numColumns = getResources().getInteger(R.integer.landscape_tablet_recipes_list_num_columns);
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), numColumns, LinearLayoutManager.VERTICAL, false);
            recipesRecyclerView.setLayoutManager(gridLayoutManager);
        }
        imageButtonReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadOnbuttonClick();
            }
        });
        mRecipesAdapter = new RecipesAdapter(new RecipesAdapter.OnClickHandler() {
            @Override
            public void OnClickIngredient(String recipe, ArrayList<Ingredient> ingredients) {
                Intent intent = new Intent(getActivity(), IngredientActivity.class);
                intent.putExtra(getString(R.string.action_ingredients), ingredients);
                intent.putExtra(Intent.EXTRA_TEXT, recipe);
                startActivity(intent);
            }

            @Override
            public void OnClickStep(ArrayList<Step> steps) {
                Intent intent = new Intent(getActivity(), StepActivity.class);
                intent.putExtra(getString(R.string.action_steps), steps);
                intent.putExtra(Intent.ACTION_CONFIGURATION_CHANGED, isTablet);
                startActivity(intent);
            }
        });
        recipesRecyclerView.setAdapter(mRecipesAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        return rootView;
    }

    private void reloadOnbuttonClick() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(getContext()) {
            ArrayList<Recipe> recipesArray = null;

            @Override
            protected void onStartLoading() {
                if (recipesArray != null)
                    deliverResult(recipesArray);
                else
                    forceLoad();
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                ArrayList<Recipe> resultArray = null;
                // retrieves recipes from the json url provided and passes to the adapter
                try {
                    if (AppConnectivity.isOnline(getContext())) {
                        List<Recipe> recipesFromUrl = RecipesService.getRecipesFromUrl();
                        resultArray = (ArrayList<Recipe>) recipesFromUrl;
                        Log.d(TAG, recipesFromUrl.toString());
                    } else {
                        return null;
                    }
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
        if (data != null) {
            mRecipesAdapter.setRecipesAdapter(data);
            linearLayoutEmptyView.setVisibility(View.GONE);
            recipesRecyclerView.setVisibility(View.VISIBLE);
        } else {
            linearLayoutEmptyView.setVisibility(View.VISIBLE);
            recipesRecyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.app_message_empty_list, Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {
        mRecipesAdapter.setRecipesAdapter(null);
    }
}
