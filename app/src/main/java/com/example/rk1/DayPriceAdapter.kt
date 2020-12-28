package com.example.rk1

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rk1.databinding.DayPriceItemBinding
import java.text.SimpleDateFormat
import java.util.*

class DayPriceAdapter(
    private val context: Context,
    private val dayPrices: List<DayPriceViewModel.DayPriceItemModel>
) : RecyclerView.Adapter<DayPriceAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: DayPriceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DayPriceViewModel.DayPriceItemModel) {
            binding.date = Date(item.ts * 1000).toString("dd MMM yyyy")
            binding.sym = item.sym
            binding.value = item.value.toString()
            binding.root.setOnClickListener{ v ->
//                val p = 1/0
                Log.i(TAG, "PEREHOD")
                val bundle = bundleOf(InfoFragment.ARG_DATE to binding.date,
                    InfoFragment.ARG_MIN to item.min,
                    InfoFragment.ARG_MAX to item.max)
                v.findNavController().navigate(R.id.action_host_fragment_to_infoFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DayPriceItemBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = dayPrices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dayPrices[position])
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}