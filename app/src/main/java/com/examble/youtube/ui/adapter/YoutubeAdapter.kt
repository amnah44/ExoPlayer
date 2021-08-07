package com.examble.youtube.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.examble.youtube.databinding.RawItemBinding
import com.examble.youtube.ui.VideoPlayerActivity
import com.examble.youtube.data.response.Youtube
import com.examble.youtube.util.Constant

class YoutubeAdapter(val youtube: Youtube, private val context: Context) :
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
        val currentItem = youtube.feed[position].items[0]

        holder.binding!!.apply {
            titleOfVideo.text = currentItem.title
            Glide.with(context).load(currentItem.art).into(postImage)
            Glide.with(context).load(currentItem.art).into(profile)
            director.text = currentItem.director
            duration.text = ",${currentItem.year}"
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context,VideoPlayerActivity::class.java)
            intent.apply {
                putExtra(Constant.VIDEO_URL,currentItem.url)
                putExtra(Constant.VIDEO_TITLE,currentItem.title)
                putExtra(Constant.VIDEO_DESCRIPTION,currentItem.description)
                putExtra(Constant.VIDEO_DURATION,currentItem.duration.toString())
                putExtra(Constant.VIDEO_YEAR,currentItem.year.toString())
                putExtra(Constant.VIDEO_DIRECTOR,currentItem.director)
                putExtra(Constant.VIDEO_ART,currentItem.art)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount():Int{

        return youtube.feed.count()
    }

}