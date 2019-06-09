package osc.androiddevacademy.movieapp.presentation

import osc.androiddevacademy.movieapp.database.MoviesDatabase
import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.model.response.MoviesResponse
import osc.androiddevacademy.movieapp.networking.interactors.MovieInteractor
import osc.androiddevacademy.movieapp.ui.popularMovies.PopularMoviesContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularMoviesPresenter(
    private val interactor: MovieInteractor,
    private val appDatabase: MoviesDatabase
): PopularMoviesContract.Presenter {
    private lateinit var view: PopularMoviesContract.View

    override fun setView(view: PopularMoviesContract.View) {
        this.view = view
    }

    override fun onRequestPopularMovies() {
        interactor.getPopularMovies(popularMoviesCallback())
    }

    private fun popularMoviesCallback(): Callback<MoviesResponse> =
        object : Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                t.message?.let { view.onPopularMoviesFailed(it) }
            }

            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                if (response.isSuccessful) {
                    val favoriteMovies = appDatabase.moviesDao().getFavoriteMovies()
                    response.body()?.movies?.forEach {
                        if (favoriteMovies.contains(it))
                            it.isFavorite = true
                    }
                    response.body()?.movies?.let { view.onPopularMoviesRecieved(it) }
                }
            }
        }


    override fun onFavoriteClicked(movie: Movie) {
        if(movie.isFavorite){
            appDatabase.moviesDao().deleteFavoriteMovie(movie)
            movie.isFavorite = !movie.isFavorite
            view.onFavoriteRemoved(movie.title)
        }
        else {
            movie.isFavorite = !movie.isFavorite
            appDatabase.moviesDao().addFavoriteMovie(movie)
            view.onFavoriteAdded(movie.title)
        }
    }
}
