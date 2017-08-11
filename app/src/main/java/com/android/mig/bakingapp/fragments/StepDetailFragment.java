package com.android.mig.bakingapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.activities.FullscreenVideoActivity;
import com.android.mig.bakingapp.models.Step;
import com.android.mig.bakingapp.adapters.StepDetailPagerAdapter;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class StepDetailFragment extends Fragment {

    private static final int STARTING_POSITION = 0;
    private boolean isTabletFlag = false;                 // true if device is a tablet, false if it's a handset
    public ViewPager mViewPager;
    private static int mCurrentViewPagerPosition;
    View rootView;
    ArrayList<Step> mStepArrayList;

    public StepDetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        if (!isTabletFlag){
            // retrieves the array of steps that was passed from StepListFragment
            mStepArrayList = getActivity().getIntent().getParcelableArrayListExtra(Intent.EXTRA_TEXT);
            mCurrentViewPagerPosition = getActivity().getIntent().getIntExtra(Intent.EXTRA_UID, STARTING_POSITION);
        }

        // Create the adapter that will return a fragment for each step
        StepDetailPagerAdapter mStepDetailPagerAdapter = new StepDetailPagerAdapter(getChildFragmentManager());
        mStepDetailPagerAdapter.setStepDetailAdapter(mStepArrayList);

        // Set up the ViewPager with the sections adapter and displays the step that was selected
        mViewPager = (ViewPager)rootView.findViewById(R.id.viewpager_step_detail);
        mViewPager.setAdapter(mStepDetailPagerAdapter);
        mViewPager.setCurrentItem(mCurrentViewPagerPosition);

        return rootView;
    }

    /**
     * Updates key data in this fragment
     *
     * @param steps array of step objects
     * @param position position of view pager to be displayed
     * @param isTablet flag that helps distinguish between tablet and handset
     */
    public void setStepsData(ArrayList<Step> steps, int position, boolean isTablet){
        mStepArrayList = steps;
        mCurrentViewPagerPosition = position;
        isTabletFlag = isTablet;
    }

    /**
     * Displays the corresponding position of view pager
     *
     * @param position the desired position to be displayed
     */
    public void changeViewPagerPosition(int position){
        mViewPager.setCurrentItem(position);
    }

    /**
     * This fragment just holds the view that goes inside the ViewPager
     */
    public static class ViewPagerSubFragment extends Fragment{

        private static final String ARG_STEP_DESCRIPTION = "step_description";
        private static final String ARG_STEP_VIDEO_URL = "videoURL";
        private static final String ARG_STEP_THUMBNAIL_URL = "thumbnailURL";
        private static final String ARG_STEP_NUMBER = "step_number";
        private static final String TAG = ViewPagerSubFragment.class.getSimpleName();
        private SimpleExoPlayer mSimpleExoPlayer;
        private SimpleExoPlayerView mSimpleExoPlayerView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View subView = inflater.inflate(R.layout.fragment_step_detail_slide_page, container, false);
            String videoURL = getArguments().getString(ARG_STEP_VIDEO_URL);
            String thumbnailURL = getArguments().getString(ARG_STEP_THUMBNAIL_URL);
            int stepNumber = getArguments().getInt(ARG_STEP_NUMBER);

            TextView textView = (TextView) subView.findViewById(R.id.text_view_step_description);
            textView.setText(getArguments().getString(ARG_STEP_DESCRIPTION));

            mSimpleExoPlayerView = (SimpleExoPlayerView) subView.findViewById(R.id.video_step_detail_exoplayer_view);
                ImageButton buttonFullScreen =(ImageButton) subView.findViewById(R.id.exo_full_screen);
                buttonFullScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentFullScreen = new Intent(getContext(), FullscreenVideoActivity.class);
                        startActivity(intentFullScreen);
                    }
                });

            // this tries to set the player with a video, but there isn't uses an image
            if ("".equals(videoURL) || videoURL == null) {
                mSimpleExoPlayerView.setVisibility(View.INVISIBLE);
                ImageView thumbnailImageView = (ImageView) subView.findViewById(R.id.thumbnail_step_detail_image_view);
                thumbnailImageView.setVisibility(View.VISIBLE);
                if ("".equals(thumbnailURL) || thumbnailURL == null) {
                    return subView;
                } else {
                    Glide.with(this).load(thumbnailURL).into(thumbnailImageView);
                }
            } else {
                initializePlayer(Uri.parse(videoURL), stepNumber);
            }

            return subView;
        }

        /**
         * Destroys the player when activity is not visible
         * (use it in onDestroy to keep playing while activity is not visible)
         */
        @Override
        public void onStop() {
            if (mSimpleExoPlayer != null) {
                mSimpleExoPlayer.stop();
                mSimpleExoPlayer.release();
                mSimpleExoPlayer = null;
            }
            super.onStop();
        }

        /**
         * Returns a new instance of this fragment with the corresponding step description to be displayed
         */
        public static ViewPagerSubFragment newInstance(Step step, int stepNumber) {
            ViewPagerSubFragment fragment = new ViewPagerSubFragment();
            Bundle args = new Bundle();
            args.putString(ARG_STEP_DESCRIPTION, step.getStepDescription());
            args.putString(ARG_STEP_VIDEO_URL, step.getStepVideoURL());
            args.putInt(ARG_STEP_NUMBER, stepNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * Creates and prepares the exoplayer
         *
         * @param videoUri a URI that contains the media file
         * @param stepNumber value that represent the step number
         */
        public void initializePlayer(Uri videoUri, int stepNumber){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
            String userAgent = Util.getUserAgent(getActivity(), "BakingAppExoPlayer");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mSimpleExoPlayer.prepare(mediaSource);
            // this prevents the video to be played in previous and next page while they're not visible
            if (mCurrentViewPagerPosition == stepNumber){
                mSimpleExoPlayer.setPlayWhenReady(true);
            }
        }


    }
}
