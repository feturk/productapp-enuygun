package com.feyzaeda.productapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.feyzaeda.productapp.databinding.FragmentCheckOutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckOutFragment : Fragment() {

    private var _binding: FragmentCheckOutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckOutBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnCompleted.setOnClickListener {
            findNavController().navigate(CheckOutFragmentDirections.actionCheckOutFragmentToProductsFragment())
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}