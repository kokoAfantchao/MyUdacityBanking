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
import com.android.mig.bakingapp.activities.StepActivity;
import com.android.mig.bakingapp.activities.StepDetailActivity;
import com.android.mig.bakingapp.models.Step;
import com.android.mig.bakingapp.adapters.StepsAdapter;

import java.util.ArrayList;


public class StepListFragment extends Fragment {

    StepsAdapter mStepsAdapter;
    View rootView;
    private boolean isTablet;

    public StepListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recipes_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        isTablet = getActivity().getIntent().getBooleanExtra(Intent.ACTION_CONFIGURATION_CHANGED , false);

        ArrayList<Step> mStepArrayList = getActivity().getIntent().getParcelableArrayListExtra(String.valueOf(R.string.action_steps));
        mStepsAdapter = new StepsAdapter();
        recyclerView.setAdapter(mStepsAdapter);
        mStepsAdapter.setStepsAdapter(mStepArrayList, new StepsAdapter.OnClickHandler() {
            @Override
            public void OnClick(ArrayList<Step> stepArrayList, int position) {
                // if it's not a table then start an activity to display steps
                if (!isTablet){
                    Intent intent = new Intent(getActivity(), StepDetailActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, stepArrayList);
                    intent.putExtra(Intent.EXTRA_UID, position);
                    startActivity(intent);
                // if it's a tablet just change the position of view pager
                } else {
                    StepActivity.sStepDetailInstance.changeViewPagerPosition(position);
                }
            }
        });

        return rootView;
    }
}
