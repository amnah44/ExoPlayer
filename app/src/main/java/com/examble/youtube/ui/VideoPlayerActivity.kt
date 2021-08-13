package com.examble.youtube.ui

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.examble.youtube.R
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

    private lateinit var pictureInPictureParams: PictureInPictureParams.Builder

    @SuppressLint("SetTextI18n", "ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Youtube)
        super.onCreate(savedInstanceState)
        _binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeExoPlayer()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureParams = PictureInPictureParams.Builder()
        }
    }

    private fun initializeExoPlayer() {
        player = SimpleExoPlayer.Builder(this).build()
        _binding.exoPlayerVideo.player = player
        buildMediaSource().let {
            player?.setMediaSource(it)
            player?.prepare()
        }
    }

    private fun buildMediaSource(): MediaSource {
        val videoUrl = intent.getStringExtra(Constant.VIDEO_URL).toString()
        val dataSourceFactory = DefaultDataSourceFactory(this, "sample")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))
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

    @SuppressLint("ObsoleteSdkInt")
    private fun getPictureInPictureMode() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//            val videoDimension = _binding.exoPlayerVideo
//            val aspectRation = Rational(videoDimension.width, videoDimension.height)
//            pictureInPictureParams.setAspectRatio(aspectRation).build()

            enterPictureInPictureMode(pictureInPictureParams.build())

        } else {
            Log.i(TAG, "Not available for this version")
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (!isInPictureInPictureMode) {
                getPictureInPictureMode()
            } else {
                Log.i(TAG, "Not available for this version")
            }
        }
    }

    companion object {
        const val TAG = "AMNAH"
    }

}