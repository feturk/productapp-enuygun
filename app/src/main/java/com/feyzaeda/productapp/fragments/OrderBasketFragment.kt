package com.feyzaeda.productapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.feyzaeda.productapp.R
import com.feyzaeda.productapp.adapters.OrderProductsAdapter
import com.feyzaeda.productapp.databinding.FragmentOrderBasketBinding
import com.feyzaeda.productapp.viewmodels.RoomDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class OrderBasketFragment : Fragment() {

    private var _binding: FragmentOrderBasketBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderProductsAdapter: OrderProductsAdapter
    private lateinit var viewModel: RoomDbViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: RoomDbViewModel by viewModels()
        viewModel = tempViewModel
        viewModel.loadOrderProducts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBasketBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.orderProductsList.observe(viewLifecycleOwner) { list ->
            orderProductsAdapter = OrderProductsAdapter(list, requireContext(), viewModel)
            binding.rvOrderMovies.adapter = orderProductsAdapter
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.txtCount.text = it.toString()
        }

        binding.btnCompleted.setOnClickListener {
            if (viewModel.orderProductsList.value.isNullOrEmpty()) {
                dialogShow(text = getString(R.string.empty_basket))
            } else {
                findNavController().navigate(OrderBasketFragmentDirections.actionOrderBasketFragmentToCheckOutFragment())
            }
        }

        binding.tbOrderProducts.setOnClickListener {
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

    @SuppressLint("MissingInflatedId")
    private fun dialogShow(text: String) {
        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog).create()
        val view = layoutInflater.inflate(R.layout.custom_dialog, null)
        val textView = view.findViewById<TextView>(R.id.txt_title_dialog)
        val button = view.findViewById<Button>(R.id.dialogDismiss_button)
        textView.text = text
        builder.setView(view)
        button.setOnClickListener {
            builder.dismiss()
            viewModel.allDeleteOrderProducts()
            findNavController().navigate(OrderBasketFragmentDirections.actionOrderBasketFragmentToProductFragment())
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}