package com.xee.app.activejob.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import com.xee.app.activejob.R
import com.xee.app.activejob.base.BaseFragment
import com.xee.app.activejob.databinding.FragmentPeopleBinding
import com.xee.app.activejob.model.Person
import com.xee.app.activejob.ui.adapter.PersonAdapter
import com.xee.app.activejob.uitils.Resource
import com.xee.app.activejob.uitils.launchPeriodicAsync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NearByPersonListFragment : BaseFragment<FragmentPeopleBinding>() {

    lateinit var personAdapter: PersonAdapter
    lateinit var people: ArrayList<Person>

    private val personViewModel: PersonViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_people

    override fun setupView() {
        setupRecyclerView()
        callGetNearByPeople()
    }

    private fun setupRecyclerView() {
        binding.apply {
            activity?.let { activity ->
                people = ArrayList()
                personAdapter = PersonAdapter(people, activity)
                recyclerViewPeople.adapter = personAdapter
            }
        }
    }


    private fun callGetNearByPeople() {
        personViewModel.users.observe(viewLifecycleOwner)  {
            people.clear()
            people.addAll(it)
            personAdapter.notifyDataSetChanged()
        }
    }



}