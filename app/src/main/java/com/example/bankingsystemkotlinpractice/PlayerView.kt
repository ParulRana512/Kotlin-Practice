package com.example.bankingsystemkotlinpractice

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PlayerView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_view)

        var mediaPlayer: MediaPlayer? = MediaPlayer.create(this, R.raw.sound_file_1)
        mediaPlayer!!.start()

    }
}
