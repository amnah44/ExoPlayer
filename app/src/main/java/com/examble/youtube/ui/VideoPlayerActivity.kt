package com.examble.youtube.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.examble.youtube.databinding.ActivityVideoPlayerBinding
import com.examble.youtube.util.Constant
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityVideoPlayerBinding

    private var player: SimpleExoPlayer? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val extraTitle = intent.getStringExtra(Constant.VIDEO_TITLE)
        val extraArt = intent.getStringExtra(Constant.VIDEO_ART)
        val extraDirector = intent.getStringExtra(Constant.VIDEO_DIRECTOR)

        getBindingView(extraTitle, extraArt, extraDirector)

        initializePlayer()
    }

    @SuppressLint("SetTextI18n")
    private fun getBindingView(extraTitle: String?, extraArt: String?, extraDirector: String?) {
        _binding.apply {
            titleOfVideoExo.text = extraTitle
            expand1.setOnClickListener { showDescription() }
            expand2.setOnClickListener { showLessDescription() }
            pressShare.setOnClickListener { showHint("share this video") }
            pressDownload.setOnClickListener { showHint("Upload this Video") }
            pressSave.setOnClickListener { showHint("Video is saved") }
            description.text = intent.getStringExtra(Constant.VIDEO_DESCRIPTION)
            viewsCount.text = "${intent.getStringExtra(Constant.VIDEO_DURATION)} views. "
            yearPublished.text = "${intent.getStringExtra(Constant.VIDEO_YEAR)} published"
            Glide.with(applicationContext).load(extraArt).into(channelProfile)
            channelName.text = extraDirector
        }
    }

    private fun showHint(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun showLessDescription() {
        _binding.expand1.visibility = View.VISIBLE
        _binding.expand2.visibility = View.GONE
        _binding.description.visibility = View.GONE
    }

    private fun showDescription() {
        _binding.expand1.visibility = View.GONE
        _binding.expand2.visibility = View.VISIBLE
        _binding.description.visibility = View.VISIBLE

    }

    private fun buildMediaSource(): MediaSource {
        val videoUrl = intent.getStringExtra(Constant.VIDEO_URL).toString()
        val dataSourceFactory = DefaultDataSourceFactory(this, "sample")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        _binding.exoPlayerVideo.player = player
        buildMediaSource().let {
            player?.setMediaSource(it)
            player?.prepare()
        }
    }

    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player?.playWhenReady = false
        if (isFinishing) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        player?.release()
    }
}