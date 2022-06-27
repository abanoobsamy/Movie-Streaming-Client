package com.example.moviesstreamingclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesstreamingclient.databinding.ActivityMoviePlayerBinding;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DatabaseReference;

public class MoviePlayerActivity extends AppCompatActivity {

    private String videoUrl;
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    private ImageView ivExoFloatingWidget;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_movie_player);

        playerView = findViewById(R.id.playerView);
        tvTitle = findViewById(R.id.tv_title_movie);
        ivExoFloatingWidget = findViewById(R.id.exo_floating_widget);

        videoUrl = getIntent().getStringExtra("videoUrl");
        String title = getIntent().getStringExtra("videoTitle");

        tvTitle.setText(title);

        playVideo(videoUrl);

        ivExoFloatingWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer.release();

                Intent intent = new Intent(MoviePlayerActivity.this, FloatingWidgetService.class);
                intent.putExtra("videoUrl", videoUrl);
                intent.putExtra("videoTitle", title);
                startService(intent);
            }
        });
    }

    private void setFullScreen() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void playVideo(String fileUrl) {

        String playInfo = Util.getUserAgent(this, "MoviesStreamingClient");
//        DefaultDataSourceFactory defaultDataSourceFactory

        LoadControl loadControl = new DefaultLoadControl();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(this).build();
        TrackSelector trackSelector = new DefaultTrackSelector(this);

        simpleExoPlayer = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .setBandwidthMeter(bandwidthMeter)
                .setLoadControl(loadControl).build();

        playerView.setPlayer(simpleExoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(fileUrl);

        simpleExoPlayer.setMediaItem(mediaItem);
        simpleExoPlayer.prepare();

        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("videoUrl", videoUrl);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String videoUrl = savedInstanceState.getString("videoUrl");
        playVideo(videoUrl);
    }

    @Override
    protected void onPause() {
        super.onPause();

        simpleExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        simpleExoPlayer.release();
    }
}