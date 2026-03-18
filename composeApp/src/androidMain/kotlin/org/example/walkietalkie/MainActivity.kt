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


class MainActivity : ComponentActivity() {

    private lateinit var webRTCManager: WebRTCManager
    private val signalingService = SignalingService()

    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            1
        )
        super.onCreate(savedInstanceState)

        webRTCManager = WebRTCManager(
            this
        ) { candidate ->

            println("ICE candidate generated")

        }

        setContent {

            App(

                onSignalReceived = { signal ->

                    when (signal.type) {

                        SignalType.OFFER -> {

                            webRTCManager.handleOffer(signal.data)

                            webRTCManager.createAnswer { answer ->

                                lifecycleScope.launch {
                                    signalingService.sendAnswer(signal.room, answer)
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

                    println("Transmission stopped")

                },

                onExit = {

                    webRTCManager.closeConnection()

                    println("User exited")

//                    finish()

                }

            )

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webRTCManager.closeConnection()
    }
}
