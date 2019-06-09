package osc.androiddevacademy.movieapp.ui.details

import osc.androiddevacademy.movieapp.model.Review

interface DetailsContract {


    interface View{

        fun onReviewsFailed(t: Throwable)
        fun onReviewsSuccessful(reviewList : List<Review>)

    }

    interface Presenter {

        fun setView (view : DetailsContract.View)
        fun onGetReviews(id: Int)

    }

}