package com.example.moviesstreamingclient;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import androidx.annotation.Nullable;

public class FloatingWidgetService extends Service {

    public FloatingWidgetService() {
    }

    private String videoUrl;
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    private WindowManager windowManager;
    private View vFloatingWidget;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            videoUrl = intent.getStringExtra("videoUrl");
            String title = intent.getStringExtra("videoTitle");

            if (windowManager != null && vFloatingWidget.isShown() && simpleExoPlayer != null) {

                windowManager.removeView(vFloatingWidget);

                vFloatingWidget = null;
                windowManager = null;

                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer.release();

                simpleExoPlayer = null;
            }

            WindowManager.LayoutParams params;

            vFloatingWidget = LayoutInflater.from(this).inflate(R.layout.custom_pop_up_window, null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                params = new WindowManager.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            } else {

                params = new WindowManager.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }

            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 200;
            params.y = 200;

            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.addView(vFloatingWidget, params);

            playerView = vFloatingWidget.findViewById(R.id.playerView);
            ImageView ivClose = vFloatingWidget.findViewById(R.id.ivDismiss);
            ImageView ivOpen = vFloatingWidget.findViewById(R.id.ivMaximize);

            LoadControl loadControl = new DefaultLoadControl();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(this).build();
            TrackSelector trackSelector = new DefaultTrackSelector(this);

            simpleExoPlayer = new SimpleExoPlayer.Builder(this)
                    .setTrackSelector(trackSelector)
                    .setBandwidthMeter(bandwidthMeter)
                    .setLoadControl(loadControl).build();

            playerView.setPlayer(simpleExoPlayer);

            MediaItem mediaItem = MediaItem.fromUri(videoUrl);

            simpleExoPlayer.setMediaItem(mediaItem);

            ivOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (windowManager != null && vFloatingWidget.isShown() && simpleExoPlayer != null) {

                        windowManager.removeView(vFloatingWidget);

                        vFloatingWidget = null;
                        windowManager = null;

                        simpleExoPlayer.prepare();
                        simpleExoPlayer.release();
                        simpleExoPlayer.setPlayWhenReady(false);
                        simpleExoPlayer = null;

                        stopSelf();

                        Intent intent = new Intent(FloatingWidgetService.this, MoviePlayerActivity.class);
                        intent.putExtra("videoUrl", videoUrl);
                        intent.putExtra("videoTitle", title);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (windowManager != null && vFloatingWidget.isShown() && simpleExoPlayer != null) {

                        windowManager.removeView(vFloatingWidget);

                        vFloatingWidget = null;
                        windowManager = null;

                        simpleExoPlayer.prepare();
                        simpleExoPlayer.release();
                        simpleExoPlayer.setPlayWhenReady(false);
                        simpleExoPlayer = null;

                        stopSelf();
                    }
                }
            });

            playVideo();

            vFloatingWidget.findViewById(R.id.relativeCustomPopup).setOnTouchListener(new View.OnTouchListener() {

                private int initialX, initialY;
                private float initialTouchX, initialTouchY;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;

                            initialTouchX = motionEvent.getRawX();
                            initialTouchY = motionEvent.getRawY();
                            return true;

                        case MotionEvent.ACTION_UP:
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                            params.y = initialY + (int) (motionEvent.getRawX() - initialTouchY);

                            windowManager.updateViewLayout(vFloatingWidget, params);
                            return true;
                    }

                    return false;
                }
            });
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void playVideo() {

        LoadControl loadControl = new DefaultLoadControl();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(this).build();
        TrackSelector trackSelector = new DefaultTrackSelector(this);

        simpleExoPlayer = new SimpleExoPlayer.Builder(FloatingWidgetService.this)
                .setTrackSelector(trackSelector)
                .setBandwidthMeter(bandwidthMeter)
                .setLoadControl(loadControl).build();


        playerView.setPlayer(simpleExoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(videoUrl);

        simpleExoPlayer.setMediaItem(mediaItem);
        simpleExoPlayer.prepare();

        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (vFloatingWidget != null) {

            windowManager.removeView(vFloatingWidget);
        }
    }
}
