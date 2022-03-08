package com.lock.sharekoromeme

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    lateinit var mAdView : AdView
    var currentimage: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        loadmeme()
//my unit id :->ads:adUnitId="ca-app-pub-6816715955008902/5216833351"
    }
    private fun loadmeme(){
        val progressBar:ProgressBar=findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE
//        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"
        val imageView:ImageView=findViewById(R.id.imagememe)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                currentimage=response.getString("url")

                Glide.with(this).load(currentimage).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE
                        return false
                    }
                }).into(imageView)
            },
            { error ->
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG).show()
            }
        )

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun sharememe(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="jpg/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hi.. Lets Check This Meme $currentimage")
        val chooser=Intent.createChooser(intent,"Share This Meme using.")
        startActivity(chooser)
    }
    fun nextmeme(view: View) {
        loadmeme()
    }

}