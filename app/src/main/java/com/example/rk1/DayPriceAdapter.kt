package com.example.rk1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rk1.databinding.DayPriceItemBinding
import java.util.*

class DayPriceAdapter(
    private val context: Context,
    private val dayPrices: List<DayPriceViewModel.DayPriceItemModel>
) : RecyclerView.Adapter<DayPriceAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: DayPriceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DayPriceViewModel.DayPriceItemModel) {
            binding.date = Date(item.ts * 1000).toString()
            binding.sym = item.sym
            binding.value = item.value.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DayPriceItemBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = dayPrices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dayPrices[position])
    }
}