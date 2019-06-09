package osc.androiddevacademy.movieapp.ui.favoriteMovies

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragemnt_movie_grid.*
import osc.androiddevacademy.movieapp.App
import osc.androiddevacademy.movieapp.R
import osc.androiddevacademy.movieapp.ui.base.BaseFragment
import osc.androiddevacademy.movieapp.common.displayToast
import osc.androiddevacademy.movieapp.common.showFragment
import osc.androiddevacademy.movieapp.database.MoviesDatabase
import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.presentation.FavoriteMoviesPresenter
import osc.androiddevacademy.movieapp.ui.adapters.MoviesGridAdapter
import osc.androiddevacademy.movieapp.ui.details.MoviesPagerFragment

class FavoriteMoviesFragment : BaseFragment(),FavoriteMoviesContract.View {

    private val SPAN_COUNT = 2

    private val gridAdapter by lazy {
        MoviesGridAdapter(
            { onMovieClicked(it) },
            { onFavoriteClicked(it) })
    }

    private val presenter: FavoriteMoviesContract.Presenter by lazy {
        FavoriteMoviesPresenter(
            MoviesDatabase.getInstance(App.getAppContext()) )
    }

    override fun getLayoutResourceId(): Int = R.layout.fragment_movie_grid

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesGrid.apply {
            adapter = gridAdapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
        presenter.setView(this)
        requestFavoriteMovies()
    }

    override fun onResume() {
        super.onResume()
        presenter.setView(this)
        requestFavoriteMovies()
    }

    private fun requestFavoriteMovies() {
        presenter.onRequestFavoriteMovies()
    }

    override fun onFavoriteMoviesRecieved(movies: List<Movie>) {
        gridAdapter.setMovies(movies)
    }

    override fun onFavoriteMoviesEmpty() {
        activity?.displayToast("No favorite movies!")
    }

    private fun onMovieClicked(movie: Movie) {
        activity?.showFragment(
            R.id.mainFragmentHolder,
            MoviesPagerFragment.getInstance(
                ArrayList(gridAdapter.getMovies()),
                movie
            ),
            true
        )
    }

    private fun onFavoriteClicked(movie: Movie) {
        presenter.onFavoriteClicked(movie)
    }

    override fun onFavoriteRemoved(movie: Movie) {
        activity?.displayToast(movie.title + " is removed from favorites!")
        gridAdapter.removeMovie(movie)
    }
}
