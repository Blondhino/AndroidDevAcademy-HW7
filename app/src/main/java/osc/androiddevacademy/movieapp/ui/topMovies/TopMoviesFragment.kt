package osc.androiddevacademy.movieapp.ui.topMovies

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_movie_grid.*
import osc.androiddevacademy.movieapp.App
import osc.androiddevacademy.movieapp.R
import osc.androiddevacademy.movieapp.common.displayToast
import osc.androiddevacademy.movieapp.common.showFragment
import osc.androiddevacademy.movieapp.database.MoviesDatabase
import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.networking.BackendFactory
import osc.androiddevacademy.movieapp.presentation.TopMoviesPresenter
import osc.androiddevacademy.movieapp.ui.adapters.MoviesGridAdapter
import osc.androiddevacademy.movieapp.ui.base.BaseFragment
import osc.androiddevacademy.movieapp.ui.details.MoviesPagerFragment

class TopMoviesFragment : BaseFragment(),TopMoviesContract.View {
    private val SPAN_COUNT = 2

    private val gridAdapter by lazy {
        MoviesGridAdapter(
            { onMovieClicked(it) },
            { onFavoriteClicked(it) })
    }

    private val presenter: TopMoviesContract.Presenter by lazy {
        TopMoviesPresenter(
            BackendFactory.getMovieInteractor(),
            MoviesDatabase.getInstance(App.getAppContext())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesGrid.apply {
            adapter = gridAdapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
        requestTopMovies()
    }

    override fun onResume() {
        super.onResume()
        presenter.setView(this)
        requestTopMovies()
    }

    private fun requestTopMovies() {
        presenter.onRequestTopMovies()
    }

    override fun onTopMoviesRecieved(movies: List<Movie>) {
        gridAdapter.setMovies(movies)
    }

    override fun onTopMoviesFailed(message: String) {
        activity?.displayToast(message)
    }

    override fun getLayoutResourceId(): Int = R.layout.fragment_movie_grid

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

    override fun onFavoriteAdded(title: String) {
        activity?.displayToast("$title is added to favorites!")
        gridAdapter.notifyDataSetChanged()
    }

    override fun onFavoriteRemoved(title: String) {
        activity?.displayToast("$title is removed from favorites!")
        gridAdapter.notifyDataSetChanged()
    }
}