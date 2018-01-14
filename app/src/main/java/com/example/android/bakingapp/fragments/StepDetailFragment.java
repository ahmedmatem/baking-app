package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.example.android.bakingapp.utilities.KeyUtils.STEP_POSITION;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailFragment extends Fragment implements View.OnClickListener {
    private static final String PLAYER_CURRENT_POSITION = "player-current-position";
    private static final String PLAYER_PLAYBACK_STATE = "player-playback-state";
    private static final String PLAY_STATE = "play-state";

    private static final String ARG_STEP = "arg-step";
    private static final String ARG_STEPS_COUNT = "steps-count";

    private static SimpleExoPlayer mPlayer;
    private long mPlayerCurrentPosition = 0;
    private boolean mPlayState;

    private Step mStep;
    private int mStepPosition;
    private int mStepsCount;

    private boolean mHasVideo = false;

    private OnFragmentInteractionListener mListener;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param step Recipe step detail
     * @return A new instance of fragment StepDetailFragment.
     */
    public static StepDetailFragment newInstance(
            Step step, int stepPosition, int stepsCount) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);
        args.putInt(STEP_POSITION, stepPosition);
        args.putInt(ARG_STEPS_COUNT, stepsCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mStep = bundle.getParcelable(ARG_STEP);
            mStepPosition = bundle.getInt(STEP_POSITION);
            mStepsCount = bundle.getInt(ARG_STEPS_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);

        if(mStep == null){
            return view;
        }

        SimpleExoPlayerView playerView = (SimpleExoPlayerView)
                view.findViewById(R.id.player_view);
        if (!(mStep.mVideoURL == null || mStep.mVideoURL.isEmpty())) {
            // if video exists
            if (savedInstanceState != null &&
                    savedInstanceState.containsKey(PLAYER_CURRENT_POSITION)) {
                mPlayerCurrentPosition =
                        savedInstanceState.getLong(PLAYER_CURRENT_POSITION);
                mPlayState = mPlayer.getPlayWhenReady();
            }
            initializePlayer();
            playerView.setPlayer(mPlayer);
            playerView.setVisibility(View.VISIBLE);
            mHasVideo = true;
        } else {
            mHasVideo = false;
            playerView.setVisibility(View.GONE);
        }

        TextView shortDescription = (TextView)
                view.findViewById(R.id.tv_step_detail_short_description);
        shortDescription.setText(mStep.mShortDescription);

        TextView description = (TextView) view.findViewById(R.id.tv_step_detail_description);
        // remove #._ from the beginning of description text
        // except in introduction if step position is 0
        String friendlyDescription = mStep.mDescription.substring(3);
        if (mStepPosition != 0) {
            friendlyDescription = mStep.mDescription.substring(3);
        }
        description.setText(friendlyDescription);

        /**
         * set bottom navigation
         */
        // bottom navigation items
        ImageView prevButton = (ImageView) view.findViewById(R.id.iv_prev);
        prevButton.setOnClickListener(this);

        ImageView nextButton = (ImageView) view.findViewById(R.id.iv_next);
        nextButton.setOnClickListener(this);

        TextView stepInfo = (TextView) view.findViewById(R.id.tv_step_info);
        stepInfo.setText(mStepPosition + "/" + (mStepsCount - 1));

        // set navigation buttons state
        if (mStepPosition == 0) {
            prevButton.setEnabled(false);
            prevButton.setImageDrawable(getContext().getResources()
                    .getDrawable(R.drawable.ic_left_arrow_disabled));
        }
        if (mStepPosition == mStepsCount - 1) {
            nextButton.setEnabled(false);
            nextButton.setImageDrawable(getContext().getResources()
                    .getDrawable(R.drawable.ic_right_arrow_disabled));
        }

        /**
         * if LANDSCAPE MODE && no video
         * set and show step description and navigation
         * - by default they are set to be VISIBILITY.GONE
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !mHasVideo) {
            LinearLayout stepDescriptionLayout = (LinearLayout)
                    view.findViewById(R.id.ll_step_description_layout);
            stepDescriptionLayout.setVisibility(View.VISIBLE);

            LinearLayout bottomNavigationLayout = (LinearLayout)
                    view.findViewById(R.id.ll_bottom_navigation);
            bottomNavigationLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void initializePlayer() {
        Context context = getContext();
        // create player
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        mPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        // prepare player
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "BakingApp"), bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(
                Uri.parse(mStep.mVideoURL),
                dataSourceFactory,
                extractorsFactory,
                null,
                null
        );
        mPlayer.seekTo(mPlayerCurrentPosition);
        mPlayer.prepare(videoSource);
        mPlayer.setPlayWhenReady(mPlayState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPlayer != null) {
            outState.putLong(PLAYER_CURRENT_POSITION, mPlayer.getCurrentPosition());
            outState.putBoolean(PLAYER_PLAYBACK_STATE, mPlayer.getPlayWhenReady());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();
        int nextStep = 0;
        switch (btnId) {
            case R.id.iv_next:
                nextStep = 1;
                break;
            case R.id.iv_prev:
                nextStep = -1;
                break;
        }
        if (mListener != null) {
            mListener.onFragmentInteraction(nextStep);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int nextStep);
    }
}
