package osc.androiddevacademy.movieapp.presentation

import osc.androiddevacademy.movieapp.model.response.ReviewsResponse
import osc.androiddevacademy.movieapp.networking.interactors.MovieInteractor
import osc.androiddevacademy.movieapp.ui.details.DetailsContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsPresenter (private val apiInteractor : MovieInteractor) : DetailsContract.Presenter {

    private lateinit var view: DetailsContract.View

    override fun setView(view: DetailsContract.View) {
        this.view = view
    }

    override fun onGetReviews(id: Int) {

        apiInteractor.getReviewsForMovie(id, reviewsCallback())

    }


    private fun reviewsCallback(): Callback<ReviewsResponse> = object :
        Callback<ReviewsResponse> {
        override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
            view.onReviewsFailed(t)
        }

        override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
            if (response.isSuccessful) {
                response.body()?.reviews?.let { view.onReviewsSuccessful(it) }
            }
        }
    }
}