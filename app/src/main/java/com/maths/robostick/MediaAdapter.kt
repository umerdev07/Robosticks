package com.maths.robostick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MediaAdapter(private val mediaList: ArrayList<MediaClass>) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.meida_item, parent, false)
        return MediaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = mediaList[position]

        Glide.with(holder.itemView.context)
            .load(media.mediaUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_foreground) // Optional: placeholder image while loading
                    .error(R.drawable.ic_launcher_background) // Optional: error image if loading fails
            )
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.mediaImageView)
    }
}
