package com.xee.app.activejob.movie.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.xee.app.activejob.databinding.ItemAdmobBinding
import com.xee.app.activejob.databinding.ItemMovieBinding
import com.xee.app.activejob.model.Search
import com.xee.app.activejob.uitils.loadImage


class MoviesAdapter(
    private val moviesList: ArrayList<Search>,
    private val context: Context,
    private val onItemClick: (Search) -> Unit,
    private val onEditClick: (Search) -> Unit,
    private val onDeleteClick: (Search) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_MOVIE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_MOVIE)
            MovieViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else
            AdmobViewHolder(ItemAdmobBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = moviesList[position]
        if (item.viewType == VIEW_TYPE_MOVIE) {
            holder as MovieViewHolder
            holder.bind(item)
        } else {
            holder as AdmobViewHolder
            holder.bind()
        }


    }


    override fun getItemCount(): Int = moviesList.size

    override fun getItemViewType(position: Int): Int {
        return moviesList[position].viewType
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Search) {
            binding.apply {
                textViewTitle.text = item.Title
                textViewYear.text = item.Year
                imageViewPoster.loadImage(item.Poster)
                imageViewPoster.setOnClickListener {
                    onItemClick.invoke(item)
                }
                imageViewEdit.setOnClickListener {
                    onEditClick.invoke(item)
                }
                imageViewDelete.setOnClickListener {
                    onEditClick.invoke(item)
                }

            }
        }
    }

    inner class AdmobViewHolder(private val binding: ItemAdmobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val adLoader = AdLoader.Builder(context, "ca-app-pub-5377047071783220/6522882029")
                .forNativeAd { ad: NativeAd ->
                    Log.i("TAG", "pass: ")
                    binding.adView.setNativeAd(ad)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.i("TAG", "onAdFailedToLoad: ")
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                        .build()
                )
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

}