package me.danieleorlando.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import me.danieleorlando.bakingapp.R;
import me.danieleorlando.bakingapp.config.Constants;
import me.danieleorlando.bakingapp.model.Step;

public class StepDetailFragment extends Fragment  {

    private Step mStep;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private Handler mainHandler;

    private long currentPosition;

    public StepDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);

        if(savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(Constants.STEP);
            currentPosition = savedInstanceState.getLong(Constants.STEP_VIDEO_POSITION);
        }

        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();

        simpleExoPlayerView = view.findViewById(R.id.playerView);
        if (!mStep.getVideoURL().equals("")) {
            simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        }
        else {
            simpleExoPlayerView.setVisibility(View.GONE);
        }

        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        descriptionTv.setText(mStep.getDescription());

        return view;
    }

    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            if (currentPosition>0) player.seekTo(currentPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelable(Constants.STEP,mStep);
        currentState.putLong(Constants.STEP_VIDEO_POSITION,currentPosition);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        destroyPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        destroyPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        destroyPlayer();
    }

    public void destroyPlayer() {
        if (player!=null) {
            currentPosition = player.getCurrentPosition();
            player.stop();
            player.release();
        }
    }

    public void setStep(Step step) {
        mStep = step;
    }

    public boolean isInLandscapeMode(Context context ) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

}
