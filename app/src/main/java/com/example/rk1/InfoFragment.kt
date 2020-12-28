package com.example.rk1

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.example.rk1.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding
    private lateinit var date: String
    private var min: Float = 0.0f
    private var max: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            date = it.getString(ARG_DATE).toString()
            min = it.getFloat(ARG_MIN)
            max = it.getFloat(ARG_MAX)

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)

        Log.i(TAG, "DATE " + date + "; min " + min + "; max " + max)
        binding.maxVal.setText("max: " + max)
        binding.minVal.setText("min: " + min)

        val view = binding.root
        return view
    }

    companion object {
        const val ARG_DATE = "date"
        const val ARG_MIN = "min"
        const val ARG_MAX = "max"

    }
}