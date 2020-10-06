package com.mridx.c_mbh.ui.activity.details

import android.media.Image
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mridx.c_mbh.R
import com.mridx.c_mbh.data.ImageData
import kotlinx.android.synthetic.main.details_ui.*

class Details : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_ui)


        val bundle = intent.extras
        val imageData = bundle?.getSerializable("A1") as ImageData

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView?.transitionName = bundle.getString("A2")
        }

        Glide.with(this).load(imageData.url).into(imageView)

    }

}