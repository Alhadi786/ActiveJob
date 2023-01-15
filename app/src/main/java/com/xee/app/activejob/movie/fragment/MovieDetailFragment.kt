package com.xee.app.activejob.movie.fragment

import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.xee.app.activejob.R
import com.xee.app.activejob.base.BaseFragment
import com.xee.app.activejob.constants.INTER_STITIAL_AD_ID
import com.xee.app.activejob.databinding.FragmentMovieDetailBinding
import com.xee.app.activejob.uitils.loadImage
import com.xee.app.activejob.uitils.show

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    private val navArgs : MovieDetailFragmentArgs by navArgs()
    private var mInterstitialAd: InterstitialAd? = null

    override fun layoutId() = R.layout.fragment_movie_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadAd()
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),INTER_STITIAL_AD_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {

                    }

                    override fun onAdDismissedFullScreenContent() {
                        findNavController().navigateUp()
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

    override fun setupView() {

        binding.apply {
            imageViewPoster.loadImage(navArgs.movie.Poster, onlyFitCenter = true)
            etName.setText(navArgs.movie.Title)
            etYear.setText(navArgs.movie.Year)
            if (navArgs.action == "edit"){
                etName.isEnabled = true
                etYear.isEnabled = true
                buttonUpdate.show()
            }else{
                buttonBack.show()
            }
            buttonUpdate.setOnClickListener {
                val movie = navArgs.movie
                movie.Year = etYear.text.toString()
                movie.Title = etName.text.toString()
                val resultBundle = Bundle().apply {
                    putSerializable(
                        "movie",
                        movie
                    )
                }

                setFragmentResult("action", resultBundle)
               findNavController().navigateUp()
            }

            buttonBack.setOnClickListener {
                handleGoBack()
            }
        }

    }

    private fun handleGoBack() {
        if (mInterstitialAd!=null){
            mInterstitialAd?.show(requireActivity())
        }else{
            findNavController().navigateUp()
        }

    }
}