package com.examble.youtube.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.examble.youtube.R
import com.examble.youtube.data.network.Client
import com.examble.youtube.data.response.Youtube
import com.examble.youtube.databinding.ActivityMainBinding
import com.examble.youtube.ui.adapter.YoutubeAdapter
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Youtube)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        //handle recyclerView by Linear layout manager
        linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        _binding.youtubeRecycler.layoutManager = linearLayoutManager

        getClient()

    }


    private fun getClient() {
        Client().getResponse(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val films = gson.fromJson(body, Youtube::class.java)

                runOnUiThread {
                    _binding.youtubeRecycler.apply {
                        adapter = YoutubeAdapter(films, this@MainActivity,0)
                        setHasFixedSize(true)
                    }
                    getChipsGroupInfo(films)

                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Fail")
            }

        })
    }

    private fun getChipsGroupInfo(films : Youtube){

        _binding.apply {
            chipGroup.setOnCheckedChangeListener{_, checkId ->

                when (checkId) {
                    all.id-> {
                        _binding.youtubeRecycler.apply {
                            adapter = YoutubeAdapter(films, this@MainActivity,0)
                            setHasFixedSize(true)
                        }
                    }
                    drama.id -> {
                        _binding.youtubeRecycler.apply {
                            adapter = YoutubeAdapter(films, this@MainActivity,3)
                            setHasFixedSize(true)
                        }
                        _binding.category.text = "Drama Movies"
                    }
                    funny.id -> {
                        _binding.youtubeRecycler.apply {
                            adapter = YoutubeAdapter(films, this@MainActivity,4)
                            setHasFixedSize(true)
                        }
                        _binding.category.text = "Funny Movies"
                    }
                }
            }
        }
    }

}