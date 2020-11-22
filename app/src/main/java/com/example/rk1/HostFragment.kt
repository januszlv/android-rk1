package com.example.rk1

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rk1.databinding.FragmentHostBinding
import android.widget.Toast

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

        var fsym = "BTC"
        var ok = false

        binding.inputCurrency?.setOnKeyListener(
            View.OnKeyListener { v, keyCode, event ->
//                Log.i(TAG, keyCode.toString()+" PRESSED" + (keyCode == KeyEvent.KEYCODE_ENTER).toString() + (event.action == KeyEvent.ACTION_UP).toString())
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    Toast.makeText(context, "suppa", Toast.LENGTH_SHORT).show()
//                    Log.i(TAG, "PRESSED TRUE")
                    fsym = binding.inputCurrency.text.toString()
                    Log.i(TAG, "!!!!!fsym: " + fsym)
                    for (v in resources.getStringArray(R.array.from_units_array_values)) {
                        if (v == fsym) {
                            ok = true
                            break
                        }
                    }

                    if (ok) {
                        model.refresh(fsym, tsym, limit)
                    } else {
//                            fsym = "BTC"
                        Toast.makeText(context, getString(R.string.error_currency), Toast.LENGTH_SHORT).show()
                    }
                }
                Log.i(TAG, "PRESSED FALSE")
                false
            }
        )

//        model.refresh(fsym, tsym, limit)
        return view
    }
}