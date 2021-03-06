package me.cristiangomez.popularmovies.movie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.cristiangomez.popularmovies.BaseFragment;
import me.cristiangomez.popularmovies.R;
import me.cristiangomez.popularmovies.data.pojo.Movie;
import me.cristiangomez.popularmovies.data.pojo.Photo;
import me.cristiangomez.popularmovies.photoviewer.PhotoViewerActivity;
import me.cristiangomez.popularmovies.util.Constants;
import me.cristiangomez.popularmovies.util.DataError;
import me.cristiangomez.popularmovies.util.Utils;

public class MovieFragment extends BaseFragment implements MovieContract.View {
    @BindView(R.id.tv_movie_title)
    TextView mMovieTitleTv;
    @BindView(R.id.tv_movie_release_year)
    TextView mMovieReleaseYearTv;
    @BindView(R.id.tv_movie_duration)
    TextView mMovieDurationTv;
    @BindView(R.id.tv_movie_rating)
    TextView mMovieRatingTv;
    @BindView(R.id.tv_movie_plot)
    TextView mMoviePlotTv;
    @BindView(R.id.iv_movie_poster)
    ImageView mMoviePoster;
    private Unbinder mUnbinder;
    private Picasso mPicasso;
    private MovieContract.Presenter mPresenter;
    private Snackbar mErrorSnb;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPicasso = Picasso.with(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick(R.id.fl_movie_poster_container)
    public void onMoviePosterClick() {
        mPresenter.onMoviePosterClick();
    }

    public void showMoviePosterViewer(Photo photo) {
        Intent intent = new Intent(getContext(), PhotoViewerActivity.class);
        intent.putExtra(PhotoViewerActivity.EXTRA_PHOTO, photo);
        startActivity(intent);
    }

    @Override
    public void onMovie(Movie movie) {
        if (movie != null) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY");
            mMovieTitleTv.setText(movie.getTitle());
            if (movie.getReleaseDate() != null) {
                mMovieReleaseYearTv.setText(dateFormat.format(movie.getReleaseDate()));
            }
            if (movie.getRuntime() != 0) {
                mMovieDurationTv.setText(getString(R.string.movie_runtime, movie.getRuntime()));
            }
            mMovieRatingTv.setText(getString(R.string.movie_rating, movie.getVoteAverage()));
            mMoviePlotTv.setText(movie.getOverview());
            mMoviePoster.setContentDescription(getString(R.string.content_description_movie_poster,
                    movie.getTitle()));
            mPicasso.load(Utils.getImageUri(movie.getPosterPath(), Constants.IMAGE_SIZE))
                    .fit()
                    .centerCrop()
                    .into(mMoviePoster);
        }
    }

    public void setPresenter(MovieContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onError(DataError dataError) {
        dismissError();
        String errorString = null;
        switch (dataError) {
            case NETWORK_NOT_AVAILABLE:
                errorString = getString(R.string.error_network_not_available);
                break;
            case INVALID_API_KEY:
                errorString = getString(R.string.error_invalid_api_key);
        }
        mErrorSnb = Snackbar.make(mMovieTitleTv, errorString,
                Snackbar.LENGTH_INDEFINITE);
        mErrorSnb.setAction(R.string.error_network_not_available_action_retry,
                v -> {
                    if (mPresenter != null) {
                        mPresenter.retryMovieLoad();
                    }
                });
        mErrorSnb.show();
    }

    @Override
    public void dismissError() {
        if (mErrorSnb != null) {
            mErrorSnb.dismiss();
            mErrorSnb = null;
        }
    }
}
