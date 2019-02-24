package tech.joeyck.songsearch.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso

class ImageLoader {

    companion object {
        fun loadImage(url: String?, imageView: ImageView) {
            if(url != null && url.isNotEmpty()){
                Picasso.get().load(url).into(imageView)
            }
        }
    }

}