package com.dcard.reactionsample.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.activity_glide.*

class GlideActivity: AppCompatActivity() {

    // https://storage.googleapis.com/dcard-staging/reaction/e8e6bc5d-41b0-4129-b134-97507523d7ff1538026307422.png laugh
    //  https://storage.googleapis.com/dcard-staging/reaction/286f599c-f86a-4932-82f0-f5a06f1eca031538026283352.png heart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)

        load2()
//        preload()
    }

    private fun load(){
        Glide.with(this).asBitmap()
                .load("https://storage.googleapis.com/dcard-staging/reaction/286f599c-f86a-4932-82f0-f5a06f1eca031538026283352.png")
                .apply(RequestOptions().override(100,100))

                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return true
                    }


                })
                .into(imageView)

    }

    private fun load2(){

        Thread(Runnable {
            val bitmap =
                    Glide.with(this).asBitmap()
                            .load("https://storage.googleapis.com/dcard-staging/reaction/286f599c-f86a-4932-82f0-f5a06f1eca031538026283352.png")
//                            .apply(RequestOptions().override(100,100))

                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                    return true
                                }

                                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    return true
                                }


                            })
                            .submit().get()

            runOnUiThread {
                imageView.setImageBitmap(bitmap)
            }
        }).start()
    }

    private fun preload(){

        Glide.with(this)
                .load("https://storage.googleapis.com/dcard-staging/reaction/e8e6bc5d-41b0-4129-b134-97507523d7ff1538026307422.png")
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(e: GlideException?,
                                              model: Any?,
                                              target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean {
                        Log.d("badu","onLoadFailed")
                        Log.e("badu",e!!.message)
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?,
                                                 model: Any?,
                                                 target: Target<Drawable>?,
                                                 dataSource: DataSource?,
                                                 isFirstResource: Boolean): Boolean {
                        Log.d("badu","onResourceReady")
                        return false
                    }
                })
                .preload(50,50)
    }

}