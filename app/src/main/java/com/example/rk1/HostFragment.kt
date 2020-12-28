package com.example.rk1

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
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
import androidx.preference.Preference

class HostFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: FragmentHostBinding
    private val model: DayPriceViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var tsym: String
    private var limit: Int = 7

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

        binding.buttonWeb.setOnClickListener{
            val parsedUri: Uri = Uri.parse("https://min-api.cryptocompare.com")
            val intent = Intent(Intent.ACTION_VIEW, parsedUri)
            requireActivity().startActivity(intent)
        }

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

        tsym = sharedPreferences.getString(
            getString(R.string.pref_key),
            getString(R.string.pref_value1)
        )!!
        limit = sharedPreferences.getString(
            getString(R.string.num_key),
            "7"
        )!!.toInt()
        limit--

        var fsym = if (binding.inputCurrency.text.toString().isEmpty()) getString(R.string.btc_title) else binding.inputCurrency.text.toString()
        var ok = false

        binding.inputCurrency?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                fsym = binding.inputCurrency.text.toString()
                for (v in resources.getStringArray(R.array.from_units_array_values)) {
                    if (v == fsym) {
                        ok = true
                        break
                    }
                }

                if (ok) {
                    model.refresh(fsym, tsym, limit)
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.error_currency),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            false
        }

        model.refresh(fsym, tsym, limit)
        return view
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            getString(R.string.num_key) -> {
                limit = sharedPreferences?.getString(key, "7")!!.toInt()
            }
            getString(R.string.pref_key) -> {
                tsym = sharedPreferences?.getString(key, "usd")!!
            }
        }
    }

    override fun onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }
}