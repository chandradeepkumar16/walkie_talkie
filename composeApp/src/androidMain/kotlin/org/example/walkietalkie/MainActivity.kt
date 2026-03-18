package org.example.walkietalkie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.example.walkietalkie.model.SignalType
import org.example.walkietalkie.signaling.SignalingService
import org.example.walkietalkie.webrtc.WebRTCManager
import android.Manifest
import androidx.core.app.ActivityCompat
import android.content.Context
import android.media.MediaPlayer
import android.provider.Settings
import kotlin.time.Clock


class MainActivity : ComponentActivity() {

    private lateinit var webRTCManager: WebRTCManager
    private val signalingService = SignalingService()
    private val userId = java.util.UUID.randomUUID().toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            1
        )
        val prefs = getSharedPreferences("walkie_prefs", Context.MODE_PRIVATE)
        val deviceId = prefs.getString("device_id", null)
            ?: Clock.System.now().nanosecondsOfSecond.toString().also {

                prefs.edit().putString("device_id", it).apply()

            }
        super.onCreate(savedInstanceState)

        webRTCManager = WebRTCManager(
            this
        ) { candidate ->

            println("ICE candidate generated")

        }

        setContent {

            val savedRoom = prefs.getString("room_id", "") ?: ""

            App(
                deviceId = deviceId,
                savedRoom = savedRoom,

                onSaveRoom = { room ->
                    prefs.edit().putString("room_id", room).apply()
                },



                onSignalReceived = { signal ->

                    when (signal.type) {

                        SignalType.PING -> {

                            playPingSound()

                        }

                        SignalType.OFFER -> {

                            webRTCManager.handleOffer(signal.data)

                            webRTCManager.createAnswer { answer ->

                                lifecycleScope.launch {

                                    signalingService.sendAnswer(
                                        signal.room,
                                        answer
                                    )

                                }

                            }

                        }

                        SignalType.ANSWER -> {

                            webRTCManager.handleAnswer(signal.data)

                        }

                    }

                },

                onTalk = { room ->
                    webRTCManager.startAudio()

                    webRTCManager.createOffer { offer ->
                        lifecycleScope.launch {
                            signalingService.sendOffer(room, offer)
                        }
                    }
                },


                onStop = {
                    webRTCManager.stopAudio()
                },

                onExit = {
                    webRTCManager.stopAudio()

                    // Close peer connection
                    webRTCManager.closeConnection()

                    // Remove saved room
                    prefs.edit().remove("room_id").apply()
                },

                onToggleSpeaker = { enabled ->

                    webRTCManager.setSpeakerMode(enabled)

                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webRTCManager.closeConnection()
    }

    private fun playPingSound(){

        val mp = MediaPlayer.create(
            this,
//            Settings.System.DEFAULT_NOTIFICATION_URI
            R.raw.pingtone
        )

        mp.setOnCompletionListener {
            it.release()
        }

        mp.start()

    }
}
