package tech.joeyck.songsearch.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import tech.joeyck.songsearch.R
import tech.joeyck.songsearch.data.TrackResult
import tech.joeyck.songsearch.utils.ImageLoader
import tech.joeyck.songsearch.utils.formatMillis

class ResultsAdapter(private val context : Context, val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var items : Array<TrackResult> = emptyArray()

    fun submitItems(arr: Array<TrackResult>){
        items = arr
        notifyDataSetChanged()
    }

    fun clearItems(){
        items = emptyArray()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SongResultViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val songResult = items[position]
        val vh = holder as SongResultViewHolder
        vh.trackText.text = songResult.trackName
        vh.artistText.text = songResult.artistName
        vh.genre.text = songResult.primaryGenreName
        vh.genre.requestLayout()
        vh.priceText.text = "${songResult.trackPrice} ${songResult.currency}"
        vh.trackLengthText.text = formatMillis(songResult.trackTimeMillis?.toLong() ?: 0)
        ImageLoader.loadImage(songResult.artworkUrl100,vh.artImage)
    }

    override fun getItemCount(): Int = items.size

    inner class SongResultViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val trackText = view.text_title
        val artistText = view.text_artist
        val artImage = view.image_art
        val priceText = view.text_price
        val trackLengthText = view.text_trackLength
        val genre = view.text_genre

        init{
            view.setOnClickListener{
                onItemClick.invoke(adapterPosition)
            }
        }
    }

}