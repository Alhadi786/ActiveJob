package com.xee.app.activejob.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.afollestad.assent.GrantResult
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.google.android.gms.common.api.ResolvableApiException
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
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class NearByPersonListFragment : BaseFragment<FragmentPeopleBinding>() {

    var TAG = NearByPersonListFragment::class.java.name
    private lateinit var nearedPeopleList: ArrayList<Person>
    lateinit var personAdapter: PersonAdapter
    lateinit var people: ArrayList<Person>

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val personViewModel: PersonViewModel by viewModels()

    private var launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                startLocationUpdates()
            }
        }

    override fun layoutId() = R.layout.fragment_people

    override fun setupView() {
        setupRecyclerView()
        checkForPermission()
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
            when (it) {
                is Resource.Success -> {
                    people.clear()
                    people.addAll(it.value.people)
                    Log.i(TAG, "callGetNearByPeople: ")
                    initLocation()
                }
                is Resource.Failure -> {

                }
            }

        }
    }

    private fun checkForPermission() {

        askForPermissions(
            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_COARSE_LOCATION
        ) { grantResults ->

            val isPermissionGranted =
                grantResults[Permission.ACCESS_FINE_LOCATION].name.equals(
                    GrantResult.GRANTED.toString(),
                    true
                )

            if (isPermissionGranted) {
                callGetNearByPeople()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocation() {

        activity?.let { act ->

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(act)
            fusedLocationClient?.lastLocation

            locationRequest = createLocationRequest()

            locationRequest?.let {

                val builder = LocationSettingsRequest.Builder().addLocationRequest(it)
                val client = LocationServices.getSettingsClient(act)
                val task = client.checkLocationSettings(builder.build())

                task.addOnSuccessListener(act) {
                    startLocationUpdates()
                }

                task.addOnFailureListener(act) { e ->

                    if (e is ResolvableApiException) {

                        try {

                            val intentSenderRequest =
                                IntentSenderRequest.Builder(e.resolution).build()
                            launcher.launch(intentSenderRequest)

                        } catch (sendEx: IntentSender.SendIntentException) {

                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {

                Log.i(TAG, "onLocationResult: ")
                val locations = result.locations
                if (locations.isNotEmpty()) {

                    val currentLocation = locations[0]
                    currentLocation?.let { loc ->
                        sortAndFilterPeopleList(loc)
                        hideLoading()
                    }
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
        val random = Random()

        val lat = random.nextLong()+currentLocation.latitude
        val long = random.nextLong()+currentLocation.longitude
        currentLocation.longitude =long
        currentLocation.latitude =lat

        nearedPeopleList.clear()
        nearedPeopleList.addAll(people.filter {
            it.checkIfNearBy(currentLocation)
        }.toCollection(ArrayList()))

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


    private fun createLocationRequest() = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(5)
    ).build()

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        locationRequest?.let { request ->

            locationCallback?.let {

                fusedLocationClient?.requestLocationUpdates(
                    request,
                    it,
                    Looper.getMainLooper()
                )
            }
        }
    }
    private fun stopLocationUpdates() {

        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }


}