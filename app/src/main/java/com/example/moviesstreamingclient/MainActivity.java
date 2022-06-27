package com.example.moviesstreamingclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.moviesstreamingclient.Adapters.MovieShowAdapter;
import com.example.moviesstreamingclient.Adapters.SliderPagerAdapter;
import com.example.moviesstreamingclient.Model.OnMovieItemClickListener;
import com.example.moviesstreamingclient.Model.VideoDetails;
import com.example.moviesstreamingclient.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnMovieItemClickListener {

    private ActivityMainBinding binding;

    private DatabaseReference databaseReference;

    private MovieShowAdapter rShowAdapter;
    private SliderPagerAdapter vPagerAdapter;

    private List<VideoDetails> sliderSides, uploads, uploadListLatest, uploadListPopular,
            actionsMovies, sportsMovies, comedyMovies, romanticMovies, adventureMovies, warMovies;

//    private List<SliderSide> sliderSides;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        dialog = new ProgressDialog(this);

        //have database reference
        addAllMovies();

        //recyclerView
        iniWeekMovies();
        iniPopularMovies();

        //have Tab Layout
        setMoviesViewTab();

    }

    private void addAllMovies() {

        uploads = new ArrayList<>();
        uploadListLatest = new ArrayList<>();
        uploadListPopular = new ArrayList<>();
        actionsMovies = new ArrayList<>();
        sportsMovies = new ArrayList<>();
        comedyMovies = new ArrayList<>();
        romanticMovies = new ArrayList<>();
        adventureMovies = new ArrayList<>();
        warMovies = new ArrayList<>();

        sliderSides = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Videos");

        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    uploads.clear();
                    uploadListLatest.clear();
                    uploadListPopular.clear();
                    actionsMovies.clear();
                    sportsMovies.clear();
                    comedyMovies.clear();
                    romanticMovies.clear();
                    adventureMovies.clear();
                    warMovies.clear();

                    sliderSides.clear();

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                        VideoDetails videoDetails = dataSnapshot.getValue(VideoDetails.class);

                        if (videoDetails.getVideoType().equals("Latest Movies")) {

                            uploadListLatest.add(videoDetails);
                        }

                        if (videoDetails.getVideoType().equals("Best Popular Movies")) {

                            uploadListPopular.add(videoDetails);
                        }

                        if (videoDetails.getVideoCategory().equals("Action")) {

                            actionsMovies.add(videoDetails);
                        }

                        if (videoDetails.getVideoCategory().equals("Adventure")) {

                            adventureMovies.add(videoDetails);
                        }

                        if (videoDetails.getVideoCategory().equals("Comedy")) {

                            comedyMovies.add(videoDetails);
                        }

                        if (videoDetails.getVideoCategory().equals("Romantic")) {

                            romanticMovies.add(videoDetails);
                        }

                        if (videoDetails.getVideoCategory().equals("Sports")) {

                            sportsMovies.add(videoDetails);
                        }

                        if (videoDetails.getVideoCategory().equals("War")) {

                            warMovies.add(videoDetails);
                        }

                        if (videoDetails.getVideoSlide().equals("Slide Movies")) {

                            //slides
                            sliderSides.add(videoDetails);
                        }

                        uploads.add(videoDetails);

                    }

                    rShowAdapter.notifyDataSetChanged();

                    insideSlide();
                    vPagerAdapter.notifyDataSetChanged();

                    dialog.dismiss();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void insideSlide() {

        vPagerAdapter = new SliderPagerAdapter(this, sliderSides);
        binding.vpBanner.setAdapter(vPagerAdapter);

//        vPagerAdapter.notifyDataSetChanged();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTime(), 4000, 6000);

        binding.tabIndicator.setupWithViewPager(binding.vpBanner, true);
    }

    private void iniWeekMovies() {

        rShowAdapter = new MovieShowAdapter(this, uploadListLatest, this);
        binding.recyclerViewMainWeek.setAdapter(rShowAdapter);

        binding.recyclerViewMainWeek.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void iniPopularMovies() {

        rShowAdapter = new MovieShowAdapter(this, uploadListPopular, this);
        binding.recyclerViewMainPopular.setAdapter(rShowAdapter);

        binding.recyclerViewMainPopular.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setActionsMovies() {

        rShowAdapter = new MovieShowAdapter(this, actionsMovies, this);
        binding.recyclerViewMainTab.setAdapter(rShowAdapter);

        binding.recyclerViewMainTab.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setRomanticMovies() {

        rShowAdapter = new MovieShowAdapter(this, romanticMovies, this);
        binding.recyclerViewMainTab.setAdapter(rShowAdapter);

        binding.recyclerViewMainTab.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setAdventureMovies() {

        rShowAdapter = new MovieShowAdapter(this, adventureMovies, this);
        binding.recyclerViewMainTab.setAdapter(rShowAdapter);

        binding.recyclerViewMainTab.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setWarMovies() {

        rShowAdapter = new MovieShowAdapter(this, warMovies, this);
        binding.recyclerViewMainTab.setAdapter(rShowAdapter);

        binding.recyclerViewMainTab.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setSportsMovies() {

        rShowAdapter = new MovieShowAdapter(this, sportsMovies, this);
        binding.recyclerViewMainTab.setAdapter(rShowAdapter);

        binding.recyclerViewMainTab.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setComedyMovies() {

        rShowAdapter = new MovieShowAdapter(this, comedyMovies, this);
        binding.recyclerViewMainTab.setAdapter(rShowAdapter);

        binding.recyclerViewMainTab.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setMoviesViewTab() {

        setActionsMovies();

        binding.tabCategory.addTab(binding.tabCategory.newTab().setText("Action"));
        binding.tabCategory.addTab(binding.tabCategory.newTab().setText("Adventure"));
        binding.tabCategory.addTab(binding.tabCategory.newTab().setText("Comedy"));
        binding.tabCategory.addTab(binding.tabCategory.newTab().setText("Romantic"));
        binding.tabCategory.addTab(binding.tabCategory.newTab().setText("Sports"));
        binding.tabCategory.addTab(binding.tabCategory.newTab().setText("War"));

        binding.tabCategory.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.tabCategory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:
                        setActionsMovies();
                        break;

                    case 1:
                        setAdventureMovies();
                        break;

                    case 2:
                        setComedyMovies();
                        break;

                    case 3:
                        setRomanticMovies();
                        break;

                    case 4:
                        setSportsMovies();
                        break;

                    case 5:
                        setWarMovies();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onMovieClick(VideoDetails videoDetails, ImageView imageView) {

        Intent intent = new Intent(this, MovieDetailsActivity.class);

        intent.putExtra("videoTitle", videoDetails.getVideoTitle());
        intent.putExtra("videoDesc", videoDetails.getVideoDescription());
        intent.putExtra("videoCategory", videoDetails.getVideoCategory());
        intent.putExtra("videoUrl", videoDetails.getVideoUrl());
        intent.putExtra("videoThumb", videoDetails.getVideoThumb());

        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, imageView, "sharedName");

        startActivity(intent, options.toBundle());
    }

    public class SliderTime extends TimerTask {

        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (binding.vpBanner.getCurrentItem() < sliderSides.size() - 1) {

                        // here i say increase slider by 1 every 4s
                        // i has set in method insideSlide()
                        binding.vpBanner.setCurrentItem(binding.vpBanner.getCurrentItem() + 1);
                    }
                    else {
                        binding.vpBanner.setCurrentItem(0);
                    }
                }
            });
        }
    }
}