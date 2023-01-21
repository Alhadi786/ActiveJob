package com.xee.app.activejob.ui.fragment

import android.location.Location
import android.util.Log
import androidx.fragment.app.viewModels
import com.afollestad.assent.GrantResult
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.google.android.gms.location.*
import com.xee.app.activejob.R
import com.xee.app.activejob.base.BaseFragment
import com.xee.app.activejob.databinding.FragmentPeopleBinding
import com.xee.app.activejob.model.Person
import com.xee.app.activejob.ui.adapter.PersonAdapter
import com.xee.app.activejob.uitils.Resource
import com.xee.app.activejob.uitils.hide
import com.xee.app.activejob.uitils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NearByPersonListFragment : BaseFragment<FragmentPeopleBinding>() {

    var TAG = NearByPersonListFragment::class.java.name

    private lateinit var nearedPeopleList: ArrayList<Person>
    lateinit var personAdapter: PersonAdapter
    lateinit var people: ArrayList<Person>

    private val personViewModel: PersonViewModel by viewModels()


    override fun layoutId() = R.layout.fragment_people

    override fun setupView() {
        setupRecyclerView()
        callGetNearByPeople()
       observeLocation()
    }

    private fun observeLocation() {
        personViewModel.randomLocation.observe(viewLifecycleOwner){
            Log.i(TAG, "observeLocation: "+it.latitude+" "+ it.longitude)
            sortAndFilterPeopleList(it)
            hideLoading()
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            activity?.let { activity ->
                people = ArrayList()
                nearedPeopleList = ArrayList()
                personAdapter = PersonAdapter(nearedPeopleList, activity)
                recyclerViewPeople.adapter = personAdapter
            }
        }
    }


    private fun callGetNearByPeople() {

        personViewModel.getNearByPeople().observe(viewLifecycleOwner) {
            response ->
            when (response) {
                is Resource.Success -> {
                    people.clear()
                    people.addAll(response.value.people)
                    personViewModel.randomizeLocation()
                }
                is Resource.Failure -> {
                    hideLoading()
                }
            }

        }
    }





    private fun hideLoading() {

        binding.apply {
            shimmerLayout.stopShimmerAnimation()
            shimmerLayout.hide()
        }
    }

    private fun sortAndFilterPeopleList(currentLocation: Location) {
        nearedPeopleList.clear()
        val filteredList = people.filter {
            it.checkIfNearBy(currentLocation)
        }.toCollection(ArrayList())


        nearedPeopleList.addAll(filteredList)

        nearedPeopleList.sortWith(compareBy { it.distance })
        binding.apply {
            if (nearedPeopleList.isNotEmpty()){
                recyclerViewPeople.show()
                textViewNoResult.hide()
            }else{
                recyclerViewPeople.hide()
                textViewNoResult.show()
            }
        }


        personAdapter.notifyDataSetChanged()
    }




}