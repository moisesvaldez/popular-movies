package me.cristiangomez.popularmovies.movie;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MovieDetailViewPager extends PagerAdapter {
    private MovieOverviewView mMovieOverview;

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = "Overview";
                break;
            case 1:
                title = "Reviews";
                break;
        }
        return title;
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mMovieOverview == null) {
            mMovieOverview = new MovieOverviewView(container.getContext(), null);
        }
        container.addView(mMovieOverview, 0);
        return mMovieOverview;
    }

    public MovieOverviewView getMovieOverview() {
        return mMovieOverview;
    }
}