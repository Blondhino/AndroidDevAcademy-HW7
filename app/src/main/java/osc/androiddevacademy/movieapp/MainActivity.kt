package osc.androiddevacademy.movieapp

import android.view.MenuItem
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_movies.*
import osc.androiddevacademy.movieapp.common.showFragment
import osc.androiddevacademy.movieapp.ui.base.BaseActivity
import osc.androiddevacademy.movieapp.ui.favoriteMovies.FavoriteMoviesFragment
import osc.androiddevacademy.movieapp.ui.popularMovies.PopularMoviesFragment
import osc.androiddevacademy.movieapp.ui.topMovies.TopMoviesFragment

class MainActivity : BaseActivity() {

    override fun getLayoutResourceId(): Int = R.layout.activity_movies

    override fun setUpUi() {
        initMoviesGridFragment()
    }

    private fun initMoviesGridFragment(){
        showFragment(R.id.mainFragmentHolder,
            PopularMoviesFragment()
        )
        bottomNavigation.setOnNavigationItemSelectedListener { selectFragment(it) }
    }

    private fun selectFragment(it: MenuItem): Boolean {
        var selectedFragment: Fragment? = null
        when (it.itemId) {
            R.id.navPopular -> selectedFragment =
                PopularMoviesFragment()
            R.id.navFavorite -> selectedFragment = FavoriteMoviesFragment()
            R.id.navTop -> selectedFragment = TopMoviesFragment()
        }
        if (selectedFragment != null) {
            showFragment(R.id.mainFragmentHolder,selectedFragment)
        }
        return true
    }

}