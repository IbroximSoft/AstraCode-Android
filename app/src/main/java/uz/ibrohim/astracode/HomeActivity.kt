package uz.ibrohim.astracode

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import uz.ibrohim.astracode.databinding.ActivityHomeBinding
import uz.ibrohim.astracode.databinding.OfflineItemBinding
import uz.ibrohim.astracode.internet_check.NetworkHelper

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val webUrl: String = "https://app.astracode.net/"
    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkChecking()
        onBack()

        binding.apply {
            webView.webViewClient = WebViewClient()

            webView.apply {
                webVIewSetting(webView)
                if (savedInstanceState != null) {
                    webView.restoreState(savedInstanceState)
                } else {
                    loadUrl(webUrl)
                }
            }

            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, progress: Int) {
                    if (progress == 100) {
                        homeProgress.isVisible = false
                        webView.isVisible = true
                    } else {
                        webView.isVisible = false
                        homeProgress.isVisible = true
                    }
                }
            }

            webView.webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    val subUrl = url?.substring(0, 15)
                    val subUrl2 = webUrl.substring(0, 15)
                    if (subUrl != subUrl2) {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.setData(Uri.parse(url))
                        startActivity(i)
                    }
                    view?.loadUrl(url!!)
                    return true
                }
            }
            webView.loadUrl(webUrl)
        }
    }

    private fun networkChecking() {
        val networkHelper = NetworkHelper(this)
        if (!networkHelper.isNetworkConnected()){
            binding.webView.isVisible = false
            val dialog = Dialog(this)
            val bindings: OfflineItemBinding = OfflineItemBinding.inflate(LayoutInflater.from(this))
            dialog.setContentView(bindings.root)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.show()

            bindings.btnInternet.setOnClickListener {
                if (networkHelper.isNetworkConnected()){
                    binding.webView.isVisible = true
                    dialog.dismiss()
                }
            }
        }
    }

    private fun onBack() {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.apply {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        finish()
                    }
                }
            }
        }

        this.onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onStart() {
        super.onStart()
        binding.webView.loadUrl(webUrl)
    }
}