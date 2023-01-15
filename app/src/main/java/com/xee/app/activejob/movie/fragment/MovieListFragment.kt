package com.xee.app.activejob.movie.fragment

import android.util.Log
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.xee.app.activejob.R
import com.xee.app.activejob.base.BaseFragment
import com.xee.app.activejob.constants.INTER_STITIAL_AD_ID
import com.xee.app.activejob.databinding.FragmentMovieListBinding
import com.xee.app.activejob.model.Search
import com.xee.app.activejob.movie.adapter.MoviesAdapter
import com.xee.app.activejob.uitils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : BaseFragment<FragmentMovieListBinding>() {

    private lateinit var selectedItem: Search
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var moviesArrayList: ArrayList<Search>
    private var mInterstitialAd: InterstitialAd? = null


    private val moviesViewModel: MoviesViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_movie_list

    override fun setupView() {
        setupRecyclerView()
        loadAd()
        moviesViewModel.getAllMoviesFromDB()?.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                callGetMovies()
            } else {
                moviesArrayList.addAll(it)

                for (position in 0..it.size + 1) {
                    if (position > 1 && (position + 1) % 3 == 0) {
                        val newItem = Search("", "", "", "", "")
                        newItem.viewType = 2
                        moviesArrayList.add(position, newItem)
                    }
                }

                moviesAdapter.notifyDataSetChanged()
            }

        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            activity?.let { activity ->
                moviesArrayList = ArrayList()
                moviesAdapter = MoviesAdapter(moviesArrayList, activity,
                    onItemClick = { movie ->
                        selectedItem = movie
                       handleItemClick()
                    },
                    onEditClick = { movie ->
                        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie, "edit")
                        findNavController().navigate(action)
                        setFragmentResultListener("action") { reqKey, bundle ->
                            if (reqKey == "action") {
                                val updatedMovie = bundle.getSerializable("movie") as Search
                                updatedMovie(updatedMovie)
                            }
                        }
                    },
                    onDeleteClick = { movie ->

                    }


                )
                recyclerViewMovies.adapter = moviesAdapter
            }

        }
    }

    private fun handleItemClick() {
        if (mInterstitialAd!=null){
            mInterstitialAd?.show(requireActivity())
        }else{
           goNext()
        }

    }

    private fun goNext() {
        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(selectedItem, "view")
        findNavController().navigate(action)
    }

    private fun updatedMovie(updatedMovie: Search) {

        val index = moviesArrayList.indexOf(updatedMovie)
        moviesViewModel.updateMovie(updatedMovie).observe(viewLifecycleOwner){
            moviesAdapter.notifyItemChanged(index)
        }
    }

    private fun callGetMovies() {

        moviesViewModel.getMovies().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val response = it.value
                    if (response.Response) {
                        moviesViewModel.storeAllMovies(response.Search)
                    }
                }
                is Resource.Failure -> {

                }
            }
        }
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(), INTER_STITIAL_AD_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {
                        Log.i("TAG", "onAdClicked: ")

                    }

                    override fun onAdDismissedFullScreenContent() {
                        goNext()
                        mInterstitialAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        mInterstitialAd = null
                    }

                    override fun onAdImpression() {

                    }

                    override fun onAdShowedFullScreenContent() {

                    }
                }
            }
        })

    }


}