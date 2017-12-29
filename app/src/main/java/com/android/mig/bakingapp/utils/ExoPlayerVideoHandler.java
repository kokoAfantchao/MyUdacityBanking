package com.android.mig.bakingapp.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.SurfaceView;

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

/**
 * Created by nestorkokoafantchao on 12/27/17.
 */

public class ExoPlayerVideoHandler {
    private static ExoPlayerVideoHandler instance;

    public static ExoPlayerVideoHandler getInstance() {
        if (instance == null) {
            instance = new ExoPlayerVideoHandler();
        }
        return instance;
    }

    private SimpleExoPlayer mPlayer;
    private Uri playerUri;
    private boolean isPlayerPlaying;
    private TrackSelector mtrackSelector;
    private LoadControl mloadControl;
    private MediaSource mMediaSource;
    private long resumePosition;
    private int resumeWindow;
    private Boolean shouldAutoPlay = true;

    private ExoPlayerVideoHandler() {
    }

    public void prepareExoPlayerForUri(@NonNull Context context, @NonNull Uri uri,
                                       @NonNull SimpleExoPlayerView exoPlayerView) {
        if (!uri.equals(playerUri) || mPlayer == null) {
            clearResumePosition();
            // Create a new player if the player is null or
            // we want to play a new video
            mtrackSelector = new DefaultTrackSelector();
            mloadControl = new DefaultLoadControl();
            mPlayer = ExoPlayerFactory.newSimpleInstance(context, mtrackSelector, mloadControl);
            String userAgent = Util.getUserAgent(context, "BakingAppExoPlayer");
            mMediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            playerUri = uri;
            // Do all the standard ExoPlayer code here...
            // Prepare the player with the source.

        }
        exoPlayerView.setPlayer(mPlayer);
        mPlayer.prepare(mMediaSource);
        mPlayer.setPlayWhenReady(true);

        mPlayer.clearVideoSurface();
        mPlayer.setVideoSurfaceView((SurfaceView) exoPlayerView.getVideoSurfaceView());
        mPlayer.seekTo(mPlayer.getCurrentPosition() + 1);


    }

    public void releaseVideoPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = null;
    }

    public void goToBackground() {
        if (mPlayer != null) {
            isPlayerPlaying = mPlayer.getPlayWhenReady();
            mPlayer.setPlayWhenReady(false);
        }
    }

    public void goToForeground() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(isPlayerPlaying);
        }
    }

    public void updateResumePosition() {
        if (mPlayer != null) {
            resumeWindow = mPlayer.getCurrentWindowIndex();
            resumePosition = Math.max(0, mPlayer.getCurrentPosition());
        }
    }

    public void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }
}
