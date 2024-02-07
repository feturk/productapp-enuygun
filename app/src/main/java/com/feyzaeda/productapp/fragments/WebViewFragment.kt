package com.feyzaeda.productapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.feyzaeda.productapp.databinding.FragmentWebViewBinding
import com.feyzaeda.productapp.util.Constants.WEBVIEW_BASE_URL
import com.feyzaeda.productapp.util.removeWhitespaces


class WebViewFragment : Fragment() {

    private lateinit var productName: String
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productName = WebViewFragmentArgs.fromBundle(requireArguments()).productName
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        val view = binding.root

        val str = productName.removeWhitespaces()
        binding.webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, request: String?): Boolean {
                view?.loadUrl(request!!)
                return true
            }
        }
        val url = WEBVIEW_BASE_URL + str
        binding.webView.loadUrl(url)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)

        binding.tbProduct.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }
}