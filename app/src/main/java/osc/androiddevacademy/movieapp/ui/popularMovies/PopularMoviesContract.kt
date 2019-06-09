package osc.androiddevacademy.movieapp.ui.popularMovies

import osc.androiddevacademy.movieapp.model.Movie

interface PopularMoviesContract {

    interface View {

        fun onPopularMoviesRecieved(movies: List<Movie>)

        fun onPopularMoviesFailed(message: String)

        fun onFavoriteAdded(title: String)

        fun onFavoriteRemoved(title: String)

    }

    interface Presenter {

        fun setView(view: PopularMoviesContract.View)

        fun onRequestPopularMovies()

        fun onFavoriteClicked(movie: Movie)

    }
}