package osc.androiddevacademy.movieapp.model.response

import com.google.gson.annotations.SerializedName
import osc.androiddevacademy.movieapp.model.Movie

class MoviesResponse(@SerializedName("results") val movies: ArrayList<Movie>)