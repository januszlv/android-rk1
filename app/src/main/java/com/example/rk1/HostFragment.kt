package com.example.rk1

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rk1.databinding.FragmentHostBinding

class HostFragment : Fragment() {
    private lateinit var binding: FragmentHostBinding
    private val model: DayPriceViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val dayItems = mutableListOf<DayPriceViewModel.DayPriceItemModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_host, container, false)

        val dayListAdapter = DayPriceAdapter(requireContext(), dayItems)
        val dayListManager = LinearLayoutManager(requireContext())

        binding.dayPriceItemList.apply {
            setHasFixedSize(true)
            adapter = dayListAdapter
            layoutManager = dayListManager
        }

        model.getPrice().observe(viewLifecycleOwner) { listSnapshot ->
            Log.i("TAG", "New search items from view model")
            dayItems.clear()
            dayItems.addAll(listSnapshot)
            dayListAdapter.notifyDataSetChanged()
        }

        val view = binding.root

        val tsym = sharedPreferences.getString(
            getString(R.string.pref_key),
            getString(R.string.pref_value1)
        )!!
        val limit = sharedPreferences.getString(
            getString(R.string.num_key),
            "7"
        )!!.toInt()

        val fsym = "BTC"

        model.refresh(fsym, tsym, limit)
        return view
    }
}