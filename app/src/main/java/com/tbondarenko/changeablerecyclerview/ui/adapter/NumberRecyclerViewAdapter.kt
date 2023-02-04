package com.tbondarenko.changeablerecyclerview.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbondarenko.changeablerecyclerview.data.model.Number
import com.tbondarenko.changeablerecyclerview.databinding.ItemNumberBinding

class NumberRecyclerViewAdapter(private val onDeleteButtonClicked: (Number) -> Unit) :
    ListAdapter<Number, NumberRecyclerViewAdapter.NumberViewHolder>(DiffCallBack) {

    private var numberList = mutableListOf<Number>()

    fun add(list: List<Number>) {
        numberList = list.toMutableList()
        submitList(numberList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        return NumberViewHolder(
            ItemNumberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NumberViewHolder(private val binding: ItemNumberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(number: Number) {
            with(binding) {
                numberTextView.text = number.number.toString()
                deleteNumberButton.setOnClickListener {
                    val position = adapterPosition
                    onDeleteButtonClicked(getItem(position))
                }
            }
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Number>() {
            override fun areItemsTheSame(oldItem: Number, newItem: Number): Boolean {
                return oldItem.number == newItem.number
            }

            override fun areContentsTheSame(oldItem: Number, newItem: Number): Boolean {
                return oldItem == newItem
            }
        }
    }
}