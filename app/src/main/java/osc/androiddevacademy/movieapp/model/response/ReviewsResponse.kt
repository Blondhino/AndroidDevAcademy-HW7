package osc.androiddevacademy.movieapp.model.response

import com.google.gson.annotations.SerializedName
import osc.androiddevacademy.movieapp.model.Review

data class ReviewsResponse(
    @SerializedName("results") val reviews : List<Review>
)