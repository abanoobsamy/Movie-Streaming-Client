package com.example.moviesstreamingclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.moviesstreamingclient.Adapters.MovieShowAdapter;
import com.example.moviesstreamingclient.Model.OnMovieItemClickListener;
import com.example.moviesstreamingclient.Model.VideoDetails;
import com.example.moviesstreamingclient.databinding.ActivityMovieDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements OnMovieItemClickListener {

    private ActivityMovieDetailsBinding binding;

    private DatabaseReference databaseReference;

    private List<VideoDetails> actionsMovies, sportsMovies, comedyMovies,
            romanticMovies, adventureMovies, warMovies;

    private MovieShowAdapter showAdapter;

    private String currentVideoUrl, currentVideoCategory, currentVideoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inViews();

        similarMoviesRecycler();
        similarMovies();

        binding.floatingActionStartMovieDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MovieDetailsActivity.this, MoviePlayerActivity.class);
                intent.putExtra("videoTitle", currentVideoTitle);
                intent.putExtra("videoUrl", currentVideoUrl);
                startActivity(intent);
            }
        });
    }

    private void similarMovies() {

        if (currentVideoCategory.equals("Action")) {

            showAdapter = new MovieShowAdapter(this, actionsMovies, this);
            binding.recyclerViewMovieDetails.setAdapter(showAdapter);

            showAdapter.notifyDataSetChanged();

            binding.recyclerViewMovieDetails.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
        }

        if (currentVideoCategory.equals("Adventure")) {

            showAdapter = new MovieShowAdapter(this, adventureMovies, this);
            binding.recyclerViewMovieDetails.setAdapter(showAdapter);

            showAdapter.notifyDataSetChanged();

            binding.recyclerViewMovieDetails.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
        }

        if (currentVideoCategory.equals("Sports")) {

            showAdapter = new MovieShowAdapter(this, sportsMovies, this);
            binding.recyclerViewMovieDetails.setAdapter(showAdapter);

            showAdapter.notifyDataSetChanged();

            binding.recyclerViewMovieDetails.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
        }

        if (currentVideoCategory.equals("War")) {

            showAdapter = new MovieShowAdapter(this, warMovies, this);
            binding.recyclerViewMovieDetails.setAdapter(showAdapter);

            showAdapter.notifyDataSetChanged();

            binding.recyclerViewMovieDetails.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
        }

        if (currentVideoCategory.equals("Romantic")) {

            showAdapter = new MovieShowAdapter(this, romanticMovies, this);
            binding.recyclerViewMovieDetails.setAdapter(showAdapter);

            showAdapter.notifyDataSetChanged();

            binding.recyclerViewMovieDetails.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
        }

        if (currentVideoCategory.equals("Comedy")) {

            showAdapter = new MovieShowAdapter(this, comedyMovies, this);
            binding.recyclerViewMovieDetails.setAdapter(showAdapter);

            showAdapter.notifyDataSetChanged();

            binding.recyclerViewMovieDetails.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));
        }
    }


    private void inViews() {

        Intent intent = getIntent();

        String title = intent.getStringExtra("videoTitle");
        String desc = intent.getStringExtra("videoDesc");
        String category = intent.getStringExtra("videoCategory");
        String url = intent.getStringExtra("videoUrl");
        String thumb = intent.getStringExtra("videoThumb");

        currentVideoCategory = category;
        currentVideoUrl = url;
        currentVideoTitle = title;

        Glide.with(this).load(thumb).into(binding.ivProfileSlideMovieDetails);
        Glide.with(this).load(thumb).into(binding.ivSlideMovieDetails);
        binding.tvTitleMovieDetails.setText(title);
        binding.tvDescriptionMovieDetails.setText(desc);
    }

    private void similarMoviesRecycler() {

        actionsMovies = new ArrayList<>();
        sportsMovies = new ArrayList<>();
        comedyMovies = new ArrayList<>();
        romanticMovies = new ArrayList<>();
        adventureMovies = new ArrayList<>();
        warMovies = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Videos");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    actionsMovies.clear();
                    sportsMovies.clear();
                    comedyMovies.clear();
                    romanticMovies.clear();
                    adventureMovies.clear();
                    warMovies.clear();

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                        VideoDetails videoDetails = dataSnapshot.getValue(VideoDetails.class);

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


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // we made that because if we click on item recycler Similar Movies we get item
    // that in recycler it's seem to circle of recycler items ,<
    @Override
    public void onMovieClick(VideoDetails videoDetails, ImageView imageView) {

        Glide.with(this).load(videoDetails.getVideoThumb()).into(binding.ivProfileSlideMovieDetails);
        Glide.with(this).load(videoDetails.getVideoThumb()).into(binding.ivSlideMovieDetails);
        binding.tvTitleMovieDetails.setText(videoDetails.getVideoTitle());
        binding.tvDescriptionMovieDetails.setText(videoDetails.getVideoDescription());

        currentVideoUrl = videoDetails.getVideoUrl();
        currentVideoCategory = videoDetails.getVideoCategory();

        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, imageView, "sharedName");

        options.toBundle();

    }
}
