package com.example.vibescan

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.animation.AnimationUtils

class LandingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_page)

        val imageCard = findViewById<CardView>(R.id.imageCard)

        val animation : Animation = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.zoom_in)
        imageCard.startAnimation(animation)

        val image = findViewById<ImageView>(R.id.image)
        image.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }



    }
}