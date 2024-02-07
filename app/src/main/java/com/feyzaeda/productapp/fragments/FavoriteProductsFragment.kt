package com.feyzaeda.productapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.feyzaeda.productapp.adapters.FavoriteProductsAdapter
import com.feyzaeda.productapp.databinding.FragmentFavoriteProductsBinding
import com.feyzaeda.productapp.viewmodels.RoomDbViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteProductsFragment : Fragment() {

    private var _binding: FragmentFavoriteProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var favProductsAdapter: FavoriteProductsAdapter
    private lateinit var viewModel: RoomDbViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: RoomDbViewModel by viewModels()
        viewModel = tempViewModel
        viewModel.loadFavProducts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteProductsBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.favProductsList.observe(viewLifecycleOwner) { list ->
            favProductsAdapter = FavoriteProductsAdapter(list, requireContext(), viewModel) {
                findNavController().navigate(
                    FavoriteProductsFragmentDirections.actionFavoriteProductsFragmentToProductDetailFragment(
                        it
                    )
                )
            }
            binding.rvFavProducts.adapter = favProductsAdapter
        }

        binding.tbFavoriteProducts.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}