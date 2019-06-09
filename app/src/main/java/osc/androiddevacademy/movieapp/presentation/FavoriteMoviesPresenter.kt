package osc.androiddevacademy.movieapp.presentation

import osc.androiddevacademy.movieapp.database.MoviesDatabase
import osc.androiddevacademy.movieapp.ui.favoriteMovies.FavoriteMoviesContract
import osc.androiddevacademy.movieapp.model.Movie

class FavoriteMoviesPresenter(
    private val appDatabase: MoviesDatabase
) : FavoriteMoviesContract.Presenter {
    private lateinit var view: FavoriteMoviesContract.View

    override fun setView(view: FavoriteMoviesContract.View) {
        this.view = view
    }

    override fun onRequestFavoriteMovies() {
        if (appDatabase.moviesDao().getFavoriteMovies().isEmpty())
            view.onFavoriteMoviesEmpty()
        else{
            var favoriteMovies = appDatabase.moviesDao().getFavoriteMovies()
            favoriteMovies.forEach { it.isFavorite = true }
            view.onFavoriteMoviesRecieved(favoriteMovies)
        }

    }

    override fun onFavoriteClicked(movie: Movie) {
        appDatabase.moviesDao().deleteFavoriteMovie(movie)
        movie.isFavorite = !movie.isFavorite
        view.onFavoriteRemoved(movie)
    }

}