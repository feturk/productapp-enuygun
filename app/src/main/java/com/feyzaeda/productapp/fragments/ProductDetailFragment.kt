package com.feyzaeda.productapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.feyzaeda.productapp.R
import com.feyzaeda.productapp.adapters.ImageSlideAdapter
import com.feyzaeda.productapp.databinding.FragmentProductsDetailBinding
import com.feyzaeda.productapp.models.Products
import com.feyzaeda.productapp.models.ProductsRoomDbFavEntity
import com.feyzaeda.productapp.models.ProductsRoomDbOrderEntity
import com.feyzaeda.productapp.viewmodels.ProductsDetailViewModel
import com.feyzaeda.productapp.viewmodels.RoomDbViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private lateinit var productId: String
    private lateinit var viewModel: ProductsDetailViewModel
    private lateinit var viewModelDB: RoomDbViewModel
    private lateinit var imageAdapter: ImageSlideAdapter
    private var _binding: FragmentProductsDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ProductsDetailViewModel by viewModels()
        val tempViewModel2: RoomDbViewModel by viewModels()
        viewModel = tempViewModel
        viewModelDB = tempViewModel2
        productId = ProductDetailFragmentArgs.fromBundle(requireArguments()).productId.toString()
        viewModelDB.loadFavProducts()
        viewModelDB.loadOrderProducts()
        viewModel.getSingleItem(productId.toInt())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelDB.loadFavProducts()
        viewModelDB.loadFavProducts()

        viewModel.singleItem.observe(viewLifecycleOwner) { products ->
            imageAdapter = ImageSlideAdapter(requireContext(), products.images)
            binding.viewpager.adapter = imageAdapter
            binding.indicator.setViewPager(binding.viewpager)

            binding.toolbarTitle.text = products.title
            binding.tvTitle.text = products.title
            binding.tvDirector.text = products.brand
            binding.tvProductionYear.text = products.category
            binding.tvDescription.text = products.description
            binding.tvPrice.text = products.price.toString()

            binding.tvTitle.setOnClickListener {
                findNavController().navigate(
                    ProductDetailFragmentDirections.actionProductDetailFragmentToWebViewFragment(
                        products.brand!!
                    )
                )
            }
            val roomDbFavData = viewModelDB.favProductsList
            roomDbFavData.value!!.forEach {
                if (products.id!!.toInt() == it.id) {
                    binding.imgBtnFavorite.visibility = View.INVISIBLE
                    binding.imgBtnFavoriteFull.visibility = View.VISIBLE
                }
            }
            val roomDbOrderData = viewModelDB.orderProductsList
            roomDbOrderData.value!!.forEach {
                if (products.id!!.toInt() == it.id) {
                    binding.btnAddToCard.visibility = View.INVISIBLE
                    binding.btnRemoveToCard.visibility = View.VISIBLE
                }
            }

            binding.btnAddToCard.setOnClickListener {
                viewModelDB.insertOrderProducts(
                    addToOrder(products, binding.btnAddToCard, binding.btnRemoveToCard)!!
                )
            }

            binding.btnRemoveToCard.setOnClickListener {
                viewModelDB.deleteOrderProducts(
                    deleteToOrder(products, binding.btnAddToCard, binding.btnRemoveToCard)!!
                )
            }

            binding.imgBtnFavorite.setOnClickListener {
                viewModelDB.insertFavProducts(
                    addToFavorites(
                        products,
                        binding.imgBtnFavorite,
                        binding.imgBtnFavoriteFull
                    )!!
                )
            }

            binding.imgBtnFavoriteFull.setOnClickListener {
                viewModelDB.deleteFavProducts(
                    deleteToFavorites(
                        products,
                        binding.imgBtnFavorite,
                        binding.imgBtnFavoriteFull
                    )!!
                )
            }
        }
        binding.tbProductDetail.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgShopping.setOnClickListener {
            findNavController().navigate(ProductDetailFragmentDirections.actionProductDetailFragmentToOrderBasketFragment())
        }
    }

    private fun addToFavorites(
        products: Products,
        imgBtnFav: View,
        imgBtnFavFull: View
    ): ProductsRoomDbFavEntity? {
        imgBtnFav.visibility = View.INVISIBLE
        imgBtnFavFull.visibility = View.VISIBLE
        return roomDbFavEntityData(products, getString(R.string.added))
    }

    private fun deleteToFavorites(
        products: Products,
        imgBtnFav: View,
        imgBtnFavFull: View
    ): ProductsRoomDbFavEntity? {
        imgBtnFav.visibility = View.VISIBLE
        imgBtnFavFull.visibility = View.INVISIBLE
        return roomDbFavEntityData(products, getString(R.string.deleted))
    }

    private fun roomDbFavEntityData(product: Products, text: String): ProductsRoomDbFavEntity? {
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

    private fun deleteToOrder(
        products: Products,
        btnAddCard: View,
        btnRemoveCard: View
    ): ProductsRoomDbOrderEntity? {
        btnAddCard.visibility = View.VISIBLE
        btnRemoveCard.visibility = View.INVISIBLE
        return roomDbOrderEntityData(products, getString(R.string.deleted_basket))
    }

    private fun addToOrder(
        products: Products,
        btnAddCard: View,
        btnRemoveCard: View
    ): ProductsRoomDbOrderEntity? {
        btnAddCard.visibility = View.INVISIBLE
        btnRemoveCard.visibility = View.VISIBLE
        return roomDbOrderEntityData(products, getString(R.string.added_basket))
    }


    private fun roomDbOrderEntityData(
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
        viewModelDB.loadFavProducts()
        viewModelDB.loadOrderProducts()
        viewModel.getSingleItem(productId.toInt())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
