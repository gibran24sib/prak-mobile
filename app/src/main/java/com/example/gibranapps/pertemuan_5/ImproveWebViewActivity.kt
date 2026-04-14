package com.example.gibranapps.pertemuan_5

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.gibranapps.R
import com.example.gibranapps.databinding.ActivityImproveWebViewBinding
import com.google.android.material.snackbar.Snackbar

class ImproveWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImproveWebViewBinding
    private var isPageLoaded = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImproveWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        setupWebView()
        setupSwipeToRefresh()
        setupBackHandler()
    }

    private fun setupWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
                isPageLoaded = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
                isPageLoaded = true

                Handler(Looper.getMainLooper()).postDelayed({
                    hideSkeletonAndShowWeb()
                }, 500)
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                binding.progressBar.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
                Toast.makeText(this@ImproveWebViewActivity, "Error: $description", Toast.LENGTH_SHORT).show()
            }
        }

        binding.webView.loadUrl("https://playvalorant.com/id-id/news/community/")
    }

    private fun hideSkeletonAndShowWeb() {
        binding.skeletonLayout.visibility = View.GONE
        binding.webView.visibility = View.VISIBLE
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.webView.reload()
            binding.skeletonLayout.visibility = View.VISIBLE
            binding.webView.visibility = View.GONE
        }
        binding.swipeRefresh.setColorSchemeResources(
            android.R.color.holo_red_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_blue_dark
        )
    }

    private fun setupBackHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.action_search -> {
                showSearchDialog()
                true
            }
            R.id.action_share -> {
                showShareDialog()
                true
            }
            R.id.action_refresh -> {
                refreshWebView()
                true
            }
            R.id.action_settings -> {
                showSettingsDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSearchDialog() {
        val searchView = android.widget.SearchView(this)
        searchView.queryHint = "Cari di halaman..."
        searchView.setIconifiedByDefault(false)

        AlertDialog.Builder(this)
            .setTitle("🔍 Cari di Web")
            .setView(searchView)
            .setPositiveButton("Cari") { _, _ ->
                val query = searchView.query.toString()
                if (query.isNotEmpty()) {
                    binding.webView.findAllAsync(query)
                    Snackbar.make(binding.root, "Mencari: $query", Snackbar.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Masukkan kata kunci", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showShareDialog() {
        val currentUrl = binding.webView.url ?: "https://playvalorant.com/id-id/news/community/"

        AlertDialog.Builder(this)
            .setTitle("📤 Bagikan Halaman")
            .setMessage("Bagikan halaman Valorant News ini?")
            .setPositiveButton("Bagikan") { _, _ ->
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Lihat berita Valorant terbaru: $currentUrl")
                    putExtra(Intent.EXTRA_TITLE, "Valorant News")
                }
                startActivity(Intent.createChooser(shareIntent, "Bagikan melalui"))
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun refreshWebView() {
        binding.webView.reload()
        binding.skeletonLayout.visibility = View.VISIBLE
        binding.webView.visibility = View.GONE
        Snackbar.make(binding.root, "🔄 Memuat ulang halaman...", Snackbar.LENGTH_SHORT).show()
    }

    private fun showSettingsDialog() {
        val options = arrayOf(
            "🗑️ Hapus Cache",
            "ℹ️ Tentang"
        )

        AlertDialog.Builder(this)
            .setTitle("⚙️ Pengaturan WebView")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        binding.webView.clearCache(true)
                        Toast.makeText(this, "Cache berhasil dihapus", Toast.LENGTH_SHORT).show()
                    }
                    1 -> showAboutDialog()
                }
            }
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("ℹ️ Tentang")
            .setMessage("""
                ImproveWebView
                
                Fitur:
                ✅ Progress Bar di Toolbar
                ✅ Swipe to Refresh
                ✅ Skeleton Loading
                
                Menampilkan berita komunitas Valorant
                Sumber: playvalorant.com
                Versi: 1.0
            """.trimIndent())
            .setPositiveButton("Tutup", null)
            .setIcon(android.R.drawable.ic_dialog_info)
            .show()
    }
}