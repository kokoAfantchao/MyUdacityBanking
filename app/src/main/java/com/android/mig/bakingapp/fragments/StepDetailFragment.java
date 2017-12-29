package com.android.mig.bakingapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.adapters.StepDetailPagerAdapter;
import com.android.mig.bakingapp.customviews.NextPreviousIndicator;
import com.android.mig.bakingapp.models.Step;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    private static final int STARTING_POSITION = 0;
    private static final String TABLE_FLAG = "TABLE_FLAG";
    private static final String INITIAL_POSITION = "INITIAL_POSITION";
    private static final String STEP_LIST = "STEP_LIST";
    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private boolean isTabletFlag = false;// true if device is a tablet, false if it's a handset
    @BindView(R.id.viewpager_step_detail)
    ViewPager mViewPager;
    @BindView(R.id.next_previous_indicator)
    NextPreviousIndicator mNextPreviousIndicator;
    private static int mCurrentViewPagerPosition;
    View rootView;
    ArrayList<Step> mStepArrayList;

    public StepDetailFragment() {
    }

    public static StepDetailFragment newtInstance(ArrayList<Step> steps, int initialPosition, Boolean tableFlag) {
        StepDetailFragment f = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(STEP_LIST, steps);
        args.putInt(INITIAL_POSITION, initialPosition);
        args.putBoolean(TABLE_FLAG, tableFlag);
        f.setArguments(args);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        Bundle arguments = getArguments();
        if (arguments != null) {
            ArrayList<Step> stepArrayList = arguments.getParcelableArrayList(STEP_LIST);
            setStepsData(stepArrayList,
                    arguments.getInt(INITIAL_POSITION),
                    arguments.getBoolean(TABLE_FLAG)
            );
        }
        // Create the adapter that will return a fragment for each step
        StepDetailPagerAdapter mStepDetailPagerAdapter = new StepDetailPagerAdapter(getChildFragmentManager());
        mStepDetailPagerAdapter.setStepDetailAdapter(mStepArrayList);
        // Set up the ViewPager with the sections adapter and displays the step that was selected
        mViewPager.setAdapter(mStepDetailPagerAdapter);
        mViewPager.setCurrentItem(mCurrentViewPagerPosition);
        if (!isTabletFlag) {
            // retrieves the array of steps that was passed from StepListFragment
            mStepArrayList = getActivity().getIntent().getParcelableArrayListExtra(Intent.EXTRA_TEXT);
            mCurrentViewPagerPosition = getActivity().getIntent().getIntExtra(Intent.EXTRA_UID, STARTING_POSITION);
            mStepDetailPagerAdapter.setStepDetailAdapter(mStepArrayList);
            mNextPreviousIndicator.setVisibility(View.VISIBLE);
            mNextPreviousIndicator.setViewPager(mViewPager, mCurrentViewPagerPosition);
        }
        return rootView;
    }

    /**
     * Updates key data in this fragment
     *
     * @param steps    array of step objects
     * @param position position of view pager to be displayed
     * @param isTablet flag that helps distinguish between tablet and handset
     */
    public void setStepsData(ArrayList<Step> steps, int position, boolean isTablet) {
        mStepArrayList = steps;
        mCurrentViewPagerPosition = position;
        isTabletFlag = isTablet;
    }

    /**
     * Displays the corresponding position of view pager
     *
     * @param position the desired position to be displayed
     */
    public void changeViewPagerPosition(int position) {
        mViewPager.setCurrentItem(position);
    }


    /**
     * This fragment just holds the view that goes inside the ViewPager
     */
    public static class ViewPagerSubFragment extends Fragment {
        private static final String ARG_STEP_DESCRIPTION = "step_description";
        private static final String ARG_STEP_VIDEO_URL = "videoURL";
        private static final String ARG_STEP_THUMBNAIL_URL = "thumbnailURL";
        private static final String ARG_STEP_NUMBER = "step_number";
        private final String STATE_RESUME_WINDOW = "resumeWindow";
        private final String STATE_RESUME_POSITION = "resumePosition";
        private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
        private static final String TAG = ViewPagerSubFragment.class.getSimpleName();
        private static final int CURRENT_POSITION_REQUESTCODE = 19;
        private SimpleExoPlayer mSimpleExoPlayer;
        @BindView(R.id.video_step_detail_exoplayer_view)
        SimpleExoPlayerView mSimpleExoPlayerView;
        @BindView(R.id.text_view_step_description)
        TextView textView;
        @BindView(R.id.exo_full_screen)
        ImageButton buttonFullScreen;
        @BindView(R.id.thumbnail_step_detail_image_view)
        ImageView thumbnailImageView;
        private long mResumePosition;
        private int mResumeWindow;
        private boolean mExoPlayerFullscreen = false;
        private Dialog mFullScreenDialog;
        private MediaSource mediaSource;
        private Boolean shouldAutoPlay = true;
        private String videoURL = null;
        private int stepNumber;
        TrackSelector trackSelector;
        LoadControl loadControl;


        /**
         * Returns a new instance of this fragment with the corresponding step description to be displayed
         */
        public static ViewPagerSubFragment newInstance(Step step, int stepNumber) {
            ViewPagerSubFragment fragment = new ViewPagerSubFragment();
            Bundle args = new Bundle();
            args.putString(ARG_STEP_DESCRIPTION, step.getStepDescription());
            args.putString(ARG_STEP_VIDEO_URL, step.getStepVideoURL());
            args.putInt(ARG_STEP_NUMBER, stepNumber);
            args.putString(ARG_STEP_THUMBNAIL_URL, step.getStepThumbnailURL());
            fragment.setArguments(args);
            return fragment;
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View subView = inflater.inflate(R.layout.fragment_step_detail_slide_page, container, false);
            ButterKnife.bind(this, subView);
            videoURL = getArguments().getString(ARG_STEP_VIDEO_URL);
            String thumbnailURL = getArguments().getString(ARG_STEP_THUMBNAIL_URL);
            stepNumber = getArguments().getInt(ARG_STEP_NUMBER);
            textView.setText(getArguments().getString(ARG_STEP_DESCRIPTION));
            buttonFullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mExoPlayerFullscreen)
                        openFullscreenDialog();
                    else
                        closeFullscreenDialog();
                }
            });
            // this tries to set the player with a video, but there isn't uses an image
            if (TextUtils.isEmpty(videoURL)) {
                mSimpleExoPlayerView.setVisibility(View.INVISIBLE);
                thumbnailImageView.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(thumbnailURL)) {
                    return subView;
                } else {
                    Glide.with(this).load(thumbnailURL).into(thumbnailImageView);
                }
            } else {
                if (savedInstanceState != null) {
                    mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
                    mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
                    mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);

                }
                {
                    initializePlayer();
                    initFullscreenDialog();
                }
            }
            return subView;
        }

        public void initializePlayer() {
            if (mSimpleExoPlayer == null) {
                trackSelector = new DefaultTrackSelector();
                loadControl = new DefaultLoadControl();
                mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
                mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
                String userAgent = Util.getUserAgent(getActivity(), "BakingAppExoPlayer");
                mediaSource = new ExtractorMediaSource(Uri.parse(videoURL), new DefaultDataSourceFactory(
                        getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            }

            boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
            mSimpleExoPlayer.prepare(mediaSource, !haveResumePosition, false);

            // this prevents the video to be played in previous and next page while they're not visible
            if (mCurrentViewPagerPosition == stepNumber) {
                mSimpleExoPlayer.setPlayWhenReady(shouldAutoPlay);
            }
            if (haveResumePosition) {
                mSimpleExoPlayer.seekTo(mResumeWindow, mResumePosition);
            }

        }


        private void initFullscreenDialog() {

            mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                public void onBackPressed() {
                    if (mExoPlayerFullscreen)
                        closeFullscreenDialog();
                    super.onBackPressed();
                }
            };
        }


        private void openFullscreenDialog() {

            ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
            mFullScreenDialog.addContentView(mSimpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            // mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
            mExoPlayerFullscreen = true;
            mFullScreenDialog.show();
        }

        private void closeFullscreenDialog() {

            ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
            ((FrameLayout) getActivity().findViewById(R.id.media_player_frame)).addView(mSimpleExoPlayerView);
            mExoPlayerFullscreen = false;
            mFullScreenDialog.dismiss();

        }


        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
            outState.putLong(STATE_RESUME_POSITION, mResumePosition);
            outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
            super.onSaveInstanceState(outState);
        }

        /**
         * Destroys the player when activity is not visible
         * (use it in onDestroy to keep playing while activity is not visible)
         */
        @Override
        public void onStop() {
            super.onStop();

        }

        @Override
        public void onStart() {
            super.onStart();

        }

        @Override
        public void onPause() {
            super.onPause();
            if (mSimpleExoPlayerView != null && mSimpleExoPlayerView.getPlayer() != null) {
                mResumeWindow = mSimpleExoPlayerView.getPlayer().getCurrentWindowIndex();
                mResumePosition = Math.max(0, mSimpleExoPlayerView.getPlayer().getCurrentPosition());

                mSimpleExoPlayerView.getPlayer().release();
            }

            if (mFullScreenDialog != null)
                mFullScreenDialog.dismiss();

        }


        @Override
        public void onResume() {
            super.onResume();
            if (mSimpleExoPlayerView == null) {

            }
            if (mExoPlayerFullscreen) {
                ((ViewGroup) mSimpleExoPlayerView.getParent()).removeView(mSimpleExoPlayerView);
                mFullScreenDialog.addContentView(mSimpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                //   mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
                mFullScreenDialog.show();
            }

        }


    }
}
