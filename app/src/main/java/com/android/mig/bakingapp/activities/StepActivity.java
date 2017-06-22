package com.android.mig.bakingapp.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.fragments.StepDetailFragment;
import com.android.mig.bakingapp.models.Step;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private static final int INITIAL_POSITION = 0;
    private static final boolean TABLET_FLAG = true;
    public static StepDetailFragment sStepDetailInstance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        // if it's a tablet and it's on landscape mode then add a StepDetailFragment as a static
        // instance to able to handle it from this activity
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600){

            ArrayList<Step> stepArrayList = getIntent().getParcelableArrayListExtra(String.valueOf(R.string.action_steps));
            FragmentManager fragmentManager = getSupportFragmentManager();

            sStepDetailInstance = new StepDetailFragment();
            sStepDetailInstance.setStepsData(stepArrayList,INITIAL_POSITION, TABLET_FLAG);
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, sStepDetailInstance)
                    .commit();
        }
    }
}
