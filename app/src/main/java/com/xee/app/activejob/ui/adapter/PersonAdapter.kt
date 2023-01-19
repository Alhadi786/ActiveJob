package com.xee.app.activejob.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xee.app.activejob.databinding.ItemPeronBinding
import com.xee.app.activejob.model.Person
import com.xee.app.activejob.uitils.loadImage


class PersonAdapter(
    private val people: ArrayList<Person>,
    private val context: Context,

    ) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {

        return PersonViewHolder(ItemPeronBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder:PersonViewHolder, position: Int) {
        val item = people[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int = people.size


    inner class PersonViewHolder(private val binding: ItemPeronBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Person) {
            binding.apply {
                val userName = "${item.name.first} ${item.name.last}"
                textViewName.text = userName
                textViewCountry.text = item.location.country
                textViewEmail.text = item.email
                imageViewPerson.loadImage(item.picture.large)
            }
        }
    }
}