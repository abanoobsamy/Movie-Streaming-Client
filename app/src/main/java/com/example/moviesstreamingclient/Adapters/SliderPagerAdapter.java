package com.example.moviesstreamingclient.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.moviesstreamingclient.Model.VideoDetails;
import com.example.moviesstreamingclient.MoviePlayerActivity;
import com.example.moviesstreamingclient.R;
import com.example.moviesstreamingclient.databinding.SlideItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<VideoDetails> mSliderSides;

    public SliderPagerAdapter(Context mContext, List<VideoDetails> mSliderSides) {
        this.mContext = mContext;
        this.mSliderSides = mSliderSides;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_item, null);

        VideoDetails sliderSide = mSliderSides.get(position);

        SlideItemBinding binding = SlideItemBinding.bind(view);

        Glide.with(mContext).load(sliderSide.getVideoThumb()).into(binding.ivSlideItem);
        binding.tvTitleSlideItem.setText(sliderSide.getVideoTitle());

        binding.floatingActionStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MoviePlayerActivity.class);
                intent.putExtra("videoTitle", sliderSide.getVideoTitle());
                intent.putExtra("videoUrl", sliderSide.getVideoUrl());
                mContext.startActivity(intent);
            }
        });

        container.addView(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mSliderSides.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
