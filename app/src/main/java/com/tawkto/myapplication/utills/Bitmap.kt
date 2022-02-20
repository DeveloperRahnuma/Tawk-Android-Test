package com.tawkto.myapplication.utills

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso

//=========================================================
// This class is used for Bitmap and Image related work
//=========================================================


class Bitmap {
    companion object{
        fun invertImage(src : Bitmap): Bitmap {
            // create new bitmap with the same attributes(width,height)
            //as source bitmap
            var bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
            // color info
            var A : Int = 0
            var R : Int = 0
            var G : Int = 0
            var B : Int = 0

            var pixelColor : Int = 0
            // image size
            val height = src.getHeight();
            val width = src.getWidth();

            // scan through every pixel
            for (y in 0..height-1)
            {
                for (x in 0..width-1)
                {
                    // get one pixel
                    pixelColor = src.getPixel(x, y);
                    // saving alpha channel
                    A = Color.alpha(pixelColor);
                    // inverting byte for each R/G/B channel
                    R = 255 - Color.red(pixelColor);
                    G = 255 - Color.green(pixelColor);
                    B = 255 - Color.blue(pixelColor);
                    // set newly-inverted pixel to output image
                    bmOut.setPixel(x, y, Color.argb(A, R, G, B));
                }
            }

            // return final bitmap
            return bmOut;
        }


        fun bitmapSet(url : String, imageView: ImageView,inverted : Boolean){
            Picasso.get()
                .load(url)
                .into(object : com.squareup.picasso.Target {
                    override fun onBitmapLoaded(bitmap: android.graphics.Bitmap, from: Picasso.LoadedFrom) {
                        if(inverted){
                            imageView.setImageBitmap(com.tawkto.myapplication.utills.Bitmap.invertImage(bitmap))
                        }else{
                            imageView.setImageBitmap(bitmap)
                        }
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                    TODO("Not yet implemented")
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                   vew.setImageResource(R.drawable.event_image)
                    }
                })
        }
    }
}