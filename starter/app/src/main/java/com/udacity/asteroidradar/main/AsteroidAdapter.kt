package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

class AsteroidAdapter(private val clickListener: AsteroidListener) : ListAdapter<Asteroid,
        AsteroidAdapter.ViewHolder>(AsteroidDifferCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AsteroidListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(clickListener, asteroid)
    }

    class ViewHolder (private val binding: AsteroidListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AsteroidListener, item: Asteroid) {
            binding.asteroid = item
            binding.root.setOnClickListener{
                clickListener.onClick(item)
            }
            binding.executePendingBindings()
        }
    }

    companion object AsteroidDifferCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }
}

class AsteroidListener(val clickListener: (asteroidId: Long) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid.id)
}
