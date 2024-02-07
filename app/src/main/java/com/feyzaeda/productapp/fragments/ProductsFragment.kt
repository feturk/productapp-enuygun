package com.feyzaeda.productapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.feyzaeda.productapp.R
import com.feyzaeda.productapp.adapters.ProductsAdapter
import com.feyzaeda.productapp.databinding.FragmentProductsBinding
import com.feyzaeda.productapp.models.ListFragmentCommunicator
import com.feyzaeda.productapp.models.Products
import com.feyzaeda.productapp.models.ProductsRoomDbFavEntity
import com.feyzaeda.productapp.models.ProductsRoomDbOrderEntity
import com.feyzaeda.productapp.viewmodels.ProductsViewModel
import com.feyzaeda.productapp.viewmodels.RoomDbViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductsFragment : Fragment(), ListFragmentCommunicator {
    private lateinit var viewModel: ProductsViewModel
    private lateinit var viewModelDB: RoomDbViewModel
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var productsAdapter: ProductsAdapter
    private var categories = arrayOf<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ProductsViewModel by viewModels()
        val tempViewModel2: RoomDbViewModel by viewModels()
        viewModel = tempViewModel
        viewModelDB = tempViewModel2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel.getCategories()

        binding.apply {
            floatingActionButton.setOnClickListener {
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToFavoriteProductsFragment())
            }
            floatingOrderButton.setOnClickListener {
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToOrderBasketFragment())
            }
            imgBasket.setOnClickListener {
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToOrderBasketFragment())
            }
            searchBar.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    viewModel.getSearchList(it.toString())
                } else {
                    viewModel.getProductsList()
                }
            }
            btnFilter.setOnClickListener {
                showPriceFilterDialog()
            }

            btnCategory.setOnClickListener {
                viewModel.categories.observe(viewLifecycleOwner) {
                    for (categorie in it){
                        val tempArray = categories.copyOf(categories.size + 1)
                        tempArray[tempArray.size - 1] = categorie
                        categories = tempArray
                    }
                    showRadioConfirmationDialog(categories)
                }
            }
        }
        viewModelDB.orderProductsList.observe(viewLifecycleOwner) {
            binding.badgeBasketNumber.text = it.size.toString()
            viewModelDB.loadOrderProducts()
        }

        viewModelDB.favProductsList.observe(viewLifecycleOwner) {
            binding.badgeFavNumber.text = it.size.toString()
            viewModelDB.loadFavProducts()
        }

        viewModel.filteredProductLiveData.observe(viewLifecycleOwner) {
            productsAdapter.updateList(it)
        }

        viewModel.productsList.observe(viewLifecycleOwner) {
            productsAdapter = ProductsAdapter(it, requireContext(), this, viewModelDB)
            binding.rvProducts.adapter = productsAdapter
        }

        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun goToDetails(productId: Int) {
        findNavController().navigate(
            ProductsFragmentDirections.actionProductsFragmentToProductsDetailFragment(
                productId
            )
        )
    }

    override fun addToFavorites(product: Products) {
        roomDbEntityFavData(
            product,
            getString(R.string.added)
        )?.let { viewModelDB.insertFavProducts(it) }
    }

    override fun deleteToFavorites(product: Products) {
        roomDbEntityFavData(
            product,
            getString(R.string.deleted)
        )?.let { viewModelDB.deleteFavProducts(it) }
    }

    override fun addToOrders(product: Products) {
        roomDbEntityOrderData(
            product,
            getString(R.string.added_basket)
        )?.let { viewModelDB.insertOrderProducts(it) }
    }

    override fun deleteToOrders(product: Products) {
        roomDbEntityOrderData(
            product,
            getString(R.string.deleted_basket)
        )?.let { viewModelDB.deleteOrderProducts(it) }
    }

    private fun roomDbEntityFavData(
        product: Products,
        text: String
    ): ProductsRoomDbFavEntity? {
        try {
            val data = ProductsRoomDbFavEntity(
                product.id,
                product.title,
                product.description,
                product.price,
                product.discountPercentage,
                product.rating,
                product.stock,
                product.brand,
                product.category,
                product.thumbnail,
                product.images
            )
            Toast.makeText(
                requireContext(),
                product.title + " " + text,
                Toast.LENGTH_LONG
            )
                .show()
            return data
        } catch (e: Exception) {
            Toast.makeText(requireContext(), getString(R.string.error) + e, Toast.LENGTH_SHORT)
                .show()
        }
        return null
    }


    private fun roomDbEntityOrderData(
        product: Products,
        text: String
    ): ProductsRoomDbOrderEntity? {
        try {
            val data = ProductsRoomDbOrderEntity(
                product.id,
                product.title,
                product.description,
                product.price,
                product.discountPercentage,
                product.rating,
                product.stock,
                product.brand,
                product.category,
                product.thumbnail,
                product.images
            )
            Toast.makeText(
                requireContext(),
                product.title + " " + text,
                Toast.LENGTH_LONG
            )
                .show()
            return data
        } catch (e: Exception) {
            Toast.makeText(requireContext(), getString(R.string.error) + e, Toast.LENGTH_SHORT)
                .show()
        }
        return null
    }

    override fun onResume() {
        super.onResume()
        viewModel.productsListUpdate()
        viewModelDB.loadFavProducts()
        viewModelDB.loadOrderProducts()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showPriceFilterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_price_filter, null)
        val etMinPrice = dialogView.findViewById<EditText>(R.id.etMinPrice)
        val etMaxPrice = dialogView.findViewById<EditText>(R.id.etMaxPrice)
        val btnApply = dialogView.findViewById<Button>(R.id.btnApply)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val builder = AlertDialog.Builder(requireContext())

        builder.setView(dialogView)
            .setTitle("Fiyat Aralığı Belirle")
        val dialog = builder.create()
        btnApply.setOnClickListener {
            val minPrice = etMinPrice.text.toString().toInt()
            val maxPrice = etMaxPrice.text.toString().toInt()
            viewModel.updatePriceFilter(minPrice, maxPrice)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            viewModel.productsListUpdate()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showRadioConfirmationDialog(category: Array<String?>) {
        var selectedCategoriesIndex = 0
        var selectedCategories = categories[selectedCategoriesIndex]!!
        AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            .setTitle("Categories")
            .setSingleChoiceItems(category, selectedCategoriesIndex) { dialog_, which ->
                selectedCategoriesIndex = which
                selectedCategories = categories[which]!!
            }
            .setPositiveButton("Ok") { dialog, which ->
                Toast.makeText(requireContext(), "$selectedCategories Selected", Toast.LENGTH_SHORT)
                    .show()
                viewModel.getSelectCategory(selectedCategories)
                viewModel.productsCategoryListUpdate(selectedCategories)
            }
            .setNegativeButton("Clear") { dialog, which ->
                Toast.makeText(requireContext(), "Deleted Filter", Toast.LENGTH_SHORT)
                    .show()
                viewModel.productsListUpdate()
            }
            .show()
    }
}


