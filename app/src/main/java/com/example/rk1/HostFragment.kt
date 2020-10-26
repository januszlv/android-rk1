package com.example.rk1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.rk1.databinding.FragmentHostBinding

class HostFragment : Fragment() {
    private lateinit var binding: FragmentHostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_host, container, false)

        binding.button.setOnClickListener{
            val webpage: Uri = Uri.parse("https://min-api.cryptocompare.com/documentation")
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            requireActivity().startActivity(intent)
        }

        val view = binding.root
        return view
    }
}