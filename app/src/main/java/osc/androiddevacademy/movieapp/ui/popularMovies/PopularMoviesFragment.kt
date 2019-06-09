package osc.androiddevacademy.movieapp.ui.popularMovies

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragemnt_movie_grid.*
import osc.androiddevacademy.movieapp.App
import osc.androiddevacademy.movieapp.R
import osc.androiddevacademy.movieapp.common.displayToast
import osc.androiddevacademy.movieapp.common.showFragment
import osc.androiddevacademy.movieapp.database.MoviesDatabase
import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.networking.BackendFactory
import osc.androiddevacademy.movieapp.presentation.PopularMoviesPresenter
import osc.androiddevacademy.movieapp.ui.adapters.MoviesGridAdapter
import osc.androiddevacademy.movieapp.ui.base.BaseFragment
import osc.androiddevacademy.movieapp.ui.details.MoviesPagerFragment

class PopularMoviesFragment : BaseFragment(),PopularMoviesContract.View {
    private val SPAN_COUNT = 2

    private val gridAdapter by lazy {
        MoviesGridAdapter(
            { onMovieClicked(it) },
            { onFavoriteClicked(it) })
    }

    private val presenter: PopularMoviesContract.Presenter by lazy {
        PopularMoviesPresenter(
            BackendFactory.getMovieInteractor(),
            MoviesDatabase.getInstance(App.getAppContext()))
    }

    override fun getLayoutResourceId(): Int = R.layout.fragment_movie_grid

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesGrid.apply {
            adapter = gridAdapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
        requestPopularMovies()
    }
    override fun onResume() {
        super.onResume()
        presenter.setView(this)
        requestPopularMovies()
    }

    private fun requestPopularMovies() {
        presenter.onRequestPopularMovies()
    }

    override fun onPopularMoviesRecieved(movies: List<Movie>) {
        gridAdapter.setMovies(movies)
    }

    override fun onPopularMoviesFailed(message: String) {
        activity?.displayToast(message)
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

    override fun onFavoriteAdded(title: String) {
        activity?.displayToast("$title is added to favorites!")
        gridAdapter.notifyDataSetChanged()
    }

    override fun onFavoriteRemoved(title: String) {
        activity?.displayToast("$title is removed from favorites!")
        gridAdapter.notifyDataSetChanged()
    }
}