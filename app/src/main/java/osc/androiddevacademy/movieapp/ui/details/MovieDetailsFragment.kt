package osc.androiddevacademy.movieapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragemnt_details.*
import osc.androiddevacademy.movieapp.App
import osc.androiddevacademy.movieapp.R
import osc.androiddevacademy.movieapp.common.loadImage
import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.model.Review
import osc.androiddevacademy.movieapp.networking.BackendFactory
import osc.androiddevacademy.movieapp.presentation.DetailsPresenter
import osc.androiddevacademy.movieapp.ui.adapters.ReviewAdapter

class MovieDetailsFragment : Fragment(), DetailsContract.View {




    private val reviewsAdapter by lazy { ReviewAdapter() }
    private val presenter : DetailsContract.Presenter by lazy { DetailsPresenter(BackendFactory.getMovieInteractor())}


    companion object {
        private const val MOVIE_EXTRA = "movie_extra"

        fun getInstance(movie: Movie): MovieDetailsFragment {
            val fragment = MovieDetailsFragment()
            val bundle = Bundle()
            bundle.putParcelable(MOVIE_EXTRA, movie)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var movie: Movie

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragemnt_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie = arguments?.getParcelable(MOVIE_EXTRA) as Movie

        initUi()
        getReviews()
        initListeners()
    }

    private fun initUi(){
        movieImage.loadImage(movie.poster)
        movieTitle.text = movie.title
        movieOverview.text = movie.overview
        movieReleaseDate.text = movie.releaseDate
        movieVoteAverage.text = movie.averageVote.toString()

        movieReviewList.apply {
            adapter = reviewsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initListeners(){
        movieFavorite.setOnClickListener {
            onFavoriteClicked()
        }
    }

    private fun onFavoriteClicked(){

    }


    override fun onResume() {
        super.onResume()
        presenter.setView(this)
    }

    private fun getReviews(){
        presenter.onGetReviews(movie.id)
    }


    override fun onReviewsFailed(t: Throwable) {
        Toast.makeText(App.getAppContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onReviewsSuccessful(reviewList: List<Review>) {
        reviewsAdapter.setReviewList(reviewList)
    }



}