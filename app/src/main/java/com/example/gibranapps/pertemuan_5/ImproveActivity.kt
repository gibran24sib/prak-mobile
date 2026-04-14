package com.example.gibranapps.pertemuan_5

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gibranapps.R
import com.example.gibranapps.databinding.ActivityImproveBinding
import com.google.android.material.snackbar.Snackbar

class ImproveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImproveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImproveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        binding.btnWebView.setOnClickListener {
            val intent = Intent(this, ImproveWebViewActivity::class.java)
            startActivity(intent)
            finish()
        }
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
                refreshContent()
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
        searchView.queryHint = "Cari judul atau deskripsi..."
        searchView.setIconifiedByDefault(false)

        AlertDialog.Builder(this)
            .setTitle("🔍 Pencarian")
            .setView(searchView)
            .setPositiveButton("Cari") { _, _ ->
                val query = searchView.query.toString()
                if (query.isNotEmpty()) {
                    Snackbar.make(binding.root, "Mencari: $query", Snackbar.LENGTH_SHORT)
                        .setAction("Batal") { }
                        .show()
                } else {
                    Toast.makeText(this, "Masukkan kata kunci", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showShareDialog() {
        val options = arrayOf("Bagikan Teks", "Bagikan Link", "Bagikan Screenshot")

        AlertDialog.Builder(this)
            .setTitle("📤 Bagikan Aplikasi")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> shareText()
                    1 -> shareLink()
                    2 -> shareScreenshot()
                }
            }
            .show()
    }

    private fun shareText() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Halo! Saya sedang menggunakan ImproveActivity - Aplikasi keren untuk belajar Android!")
            putExtra(Intent.EXTRA_TITLE, "ImproveActivity")
        }
        startActivity(Intent.createChooser(shareIntent, "Bagikan melalui"))
    }

    private fun shareLink() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Download ImproveActivity sekarang juga! https://example.com/improveactivity")
        }
        startActivity(Intent.createChooser(shareIntent, "Bagikan Link"))
    }

    private fun shareScreenshot() {
        Toast.makeText(this, "Fitur screenshot akan segera hadir", Toast.LENGTH_SHORT).show()
    }

    private fun refreshContent() {
        binding.scrollView.smoothScrollTo(0, 0)

        Snackbar.make(binding.root, "✅ Halaman berhasil disegarkan", Snackbar.LENGTH_SHORT)
            .setAction("Kembali") {
                binding.scrollView.smoothScrollTo(0, 0)
            }
            .show()
    }

    private fun showSettingsDialog() {
        val settings = arrayOf(
            "🌙 Mode Gelap",
            "🔔 Notifikasi",
            "📏 Ukuran Teks",
            "🌐 Bahasa"
        )

        AlertDialog.Builder(this)
            .setTitle("⚙️ Pengaturan")
            .setItems(settings) { _, which ->
                when (which) {
                    0 -> showDarkModeDialog()
                    1 -> showNotificationDialog()
                    2 -> showTextSizeDialog()
                    3 -> showLanguageDialog()
                }
            }
            .setPositiveButton("Tutup", null)
            .show()
    }

    private fun showDarkModeDialog() {
        val modes = arrayOf("Terang", "Gelap", "Sistem")

        AlertDialog.Builder(this)
            .setTitle("🌙 Mode Gelap")
            .setSingleChoiceItems(modes, 0) { dialog, which ->
                when (which) {
                    0 -> Toast.makeText(this, "Mode Terang", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(this, "Mode Gelap", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this, "Mode Sistem", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun showNotificationDialog() {
        AlertDialog.Builder(this)
            .setTitle("🔔 Notifikasi")
            .setMessage("Aktifkan notifikasi untuk mendapatkan info terbaru?")
            .setPositiveButton("Ya") { _, _ ->
                Toast.makeText(this, "Notifikasi diaktifkan", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun showTextSizeDialog() {
        val sizes = arrayOf("Kecil", "Normal", "Besar", "Besar Sekali")

        AlertDialog.Builder(this)
            .setTitle("📏 Ukuran Teks")
            .setSingleChoiceItems(sizes, 1) { dialog, which ->
                when (which) {
                    0 -> Toast.makeText(this, "Ukuran teks: Kecil", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(this, "Ukuran teks: Normal", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this, "Ukuran teks: Besar", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(this, "Ukuran teks: Besar Sekali", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("Bahasa Indonesia", "English", "العربية", "中文")

        AlertDialog.Builder(this)
            .setTitle("🌐 Bahasa")
            .setSingleChoiceItems(languages, 0) { dialog, which ->
                when (which) {
                    0 -> Toast.makeText(this, "Bahasa Indonesia", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(this, "English", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this, "العربية", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(this, "中文", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .show()
    }
}