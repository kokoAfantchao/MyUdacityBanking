package com.android.mig.bakingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.models.Ingredient;
import com.android.mig.bakingapp.adapters.IngredientsAdapter;
import com.android.mig.bakingapp.utils.Preferences;

import java.util.ArrayList;


public class IngredientListFragment extends Fragment {

    View rootView;
    IngredientsAdapter mIngredientsAdapter;

    public IngredientListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.ingredients_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        String recipe = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        ArrayList<Ingredient> mIngredientArrayList = getActivity().getIntent()
                             .getParcelableArrayListExtra(getString(R.string.action_ingredients));
        mIngredientsAdapter = new IngredientsAdapter();
        recyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsAdapter.setIngredientAdapter(mIngredientArrayList);
        Preferences.deleteAllPreferences(getActivity());
        Preferences.saveIngredientList(getActivity(), recipe, mIngredientArrayList);

        return rootView;
    }
}
