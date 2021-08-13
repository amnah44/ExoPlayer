package com.examble.youtube.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.examble.youtube.data.response.Item
import com.examble.youtube.databinding.RawItemBinding
import com.examble.youtube.ui.VideoPlayerActivity
import com.examble.youtube.data.response.Youtube
import com.examble.youtube.util.Constant.VIDEO_URL
import okhttp3.internal.notifyAll

class YoutubeAdapter(val youtube: Youtube, private val context: Context, val numberOfPosition:Int) :
    RecyclerView.Adapter<YoutubeAdapter.YoutubeViewHolder>() {

    inner class YoutubeViewHolder(val binding: RawItemBinding?) :
        RecyclerView.ViewHolder(binding?.root!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeViewHolder {
        val view: RawItemBinding =
            RawItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return YoutubeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: YoutubeViewHolder, position: Int) {
        val currentItem = youtube.feed[position].items[numberOfPosition]

        holder.binding!!.apply {
            titleOfVideo.text = currentItem.title
            Glide.with(context).load(currentItem.art).into(postImage)
            Glide.with(context).load(currentItem.art).into(profile)
            director.text = currentItem.director
            duration.text = ",${currentItem.year}"
        }

        holder.binding.playVideo.setOnClickListener {
            val intent = Intent(context,VideoPlayerActivity::class.java)
            intent.apply {

                putExtra(VIDEO_URL,currentItem.url)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount():Int{

        return youtube.feed.size
    }

}