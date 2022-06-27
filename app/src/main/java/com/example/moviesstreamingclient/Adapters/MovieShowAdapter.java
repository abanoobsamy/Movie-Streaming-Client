package com.example.moviesstreamingclient.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.moviesstreamingclient.Model.OnMovieItemClickListener;
import com.example.moviesstreamingclient.Model.VideoDetails;
import com.example.moviesstreamingclient.R;
import com.example.moviesstreamingclient.databinding.MovieItemNewBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieShowAdapter extends RecyclerView.Adapter<MovieShowAdapter.MovieViewHolder> {

    private Context mContext;
    private List<VideoDetails> mVideoDetails;
    private OnMovieItemClickListener itemClickListener;

    public MovieShowAdapter(Context mContext, List<VideoDetails> mVideoDetails, OnMovieItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.mVideoDetails = mVideoDetails;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MovieViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.movie_item_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        VideoDetails videoDetails = mVideoDetails.get(position);

        holder.binding.tvTitleMovieItem.setText(videoDetails.getVideoTitle());
        Glide.with(mContext).load(videoDetails.getVideoThumb()).into(holder.binding.ivMoviesImageItem);
    }

    @Override
    public int getItemCount() {
        return mVideoDetails.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        MovieItemNewBinding binding;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = MovieItemNewBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // to send it with intent that cuz to make onClickListener interface
                    itemClickListener.onMovieClick(mVideoDetails.get(getBindingAdapterPosition())
                            , binding.ivMoviesImageItem);
                }
            });
        }
    }
}
