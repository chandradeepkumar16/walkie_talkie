package org.example.walkietalkie.webrtc

import android.content.Context
import android.util.Log
import org.webrtc.*
import android.media.AudioManager


class WebRTCManager(
    private val context: Context,
    private val onIceCandidateGenerated: (IceCandidate) -> Unit
) {

    private lateinit var peerConnectionFactory: PeerConnectionFactory

    private var peerConnection: PeerConnection? = null
    private var audioTrack: AudioTrack? = null

    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    init {
        initializeWebRTC()
        createPeerConnection()
    }

    private fun initializeWebRTC() {

        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions
                .builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions()
        )

        val options = PeerConnectionFactory.InitializationOptions
            .builder(context)
            .createInitializationOptions()

        PeerConnectionFactory.initialize(options)

        peerConnectionFactory =
            PeerConnectionFactory.builder()
                .createPeerConnectionFactory()
    }

    private fun createPeerConnection() {

        val iceServers = listOf(
            PeerConnection.IceServer.builder(
                "stun:stun.l.google.com:19302"
            ).createIceServer()
        )

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)

        peerConnection = peerConnectionFactory.createPeerConnection(
            rtcConfig,
            object : PeerConnection.Observer {

                override fun onIceCandidate(candidate: IceCandidate) {
                    println("ICE candidate generated")

                    onIceCandidateGenerated(candidate)
                }
                override fun onTrack(transceiver: RtpTransceiver?) {
                    println("WEBRTC REMOTE AUDIO TRACK RECEIVED")
                }

                override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>) {}

                override fun onAddStream(stream: MediaStream) {}

                override fun onDataChannel(channel: DataChannel) {}

                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {

                    println("ICE Connection State: $state")

                    when(state){

                        PeerConnection.IceConnectionState.CONNECTED -> {
                            println("WebRTC CONNECTED")
                        }

                        PeerConnection.IceConnectionState.COMPLETED -> {
                            println("WebRTC CONNECTION FULLY ESTABLISHED")
                        }

                        PeerConnection.IceConnectionState.DISCONNECTED -> {
                            println("WebRTC DISCONNECTED")
                        }

                        PeerConnection.IceConnectionState.FAILED -> {
                            println("WebRTC CONNECTION FAILED")
                        }

                        else -> {}
                    }
                }

                override fun onIceConnectionReceivingChange(receiving: Boolean) {}

                override fun onConnectionChange(newState: PeerConnection.PeerConnectionState) {

                    println("PeerConnection State: $newState")

                    when(newState){

                        PeerConnection.PeerConnectionState.CONNECTED -> {
                            println("PEER CONNECTED SUCCESSFULLY")
                        }

                        PeerConnection.PeerConnectionState.DISCONNECTED -> {
                            println("PEER DISCONNECTED")
                        }

                        PeerConnection.PeerConnectionState.FAILED -> {
                            println("PEER CONNECTION FAILED")
                        }

                        else -> {}
                    }
                }


                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState) {}

                override fun onSignalingChange(state: PeerConnection.SignalingState) {}

                override fun onAddTrack(
                    receiver: RtpReceiver,
                    streams: Array<out MediaStream>
                ) {}

                override fun onRemoveStream(stream: MediaStream) {}

                override fun onRenegotiationNeeded() {}
            }
        )
    }

    fun startAudio() {

        if(audioTrack == null){

            println("Creating NEW audio track")

            val constraints = MediaConstraints().apply {
                mandatory.add(
                    MediaConstraints.KeyValuePair(
                        "googEchoCancellation",
                        "true"
                    )
                )
                mandatory.add(
                    MediaConstraints.KeyValuePair(
                        "googNoiseSuppression",
                        "true"
                    )
                )
                mandatory.add(
                    MediaConstraints.KeyValuePair(
                        "googAutoGainControl",
                        "true"
                    )
                )
            }

            val audioSource =
                peerConnectionFactory.createAudioSource(constraints)

            audioTrack =
                peerConnectionFactory.createAudioTrack(
                    "audio_track",
                    audioSource
                )

            peerConnection?.addTrack(audioTrack)
        }

        audioTrack?.setEnabled(true)
    }
    fun stopAudio() {
        audioTrack?.setEnabled(false)
    }

    fun createOffer(onOfferCreated: (String) -> Unit) {

        val constraints = MediaConstraints()

        peerConnection?.createOffer(object : SdpObserver {

            override fun onCreateSuccess(desc: SessionDescription) {

                peerConnection?.setLocalDescription(this, desc)

                onOfferCreated(desc.description)
            }

            override fun onSetSuccess() {}

            override fun onCreateFailure(error: String?) {
                println("Offer creation failed: $error")
            }

            override fun onSetFailure(error: String?) {}
        }, constraints)
    }

    fun handleOffer(offer: String) {

        val sessionDescription =
            SessionDescription(
                SessionDescription.Type.OFFER,
                offer
            )

        peerConnection?.setRemoteDescription(object : SdpObserver {

            override fun onSetSuccess() {
                println("Remote offer set")
            }

            override fun onSetFailure(error: String?) {}

            override fun onCreateSuccess(p0: SessionDescription?) {}

            override fun onCreateFailure(p0: String?) {}

        }, sessionDescription)
    }

    fun createAnswer(onAnswerCreated: (String) -> Unit) {

        peerConnection?.createAnswer(object : SdpObserver {

            override fun onCreateSuccess(desc: SessionDescription) {

                peerConnection?.setLocalDescription(this, desc)

                onAnswerCreated(desc.description)
            }

            override fun onSetSuccess() {}

            override fun onCreateFailure(error: String?) {}

            override fun onSetFailure(error: String?) {}

        }, MediaConstraints())
    }

    fun handleAnswer(answer: String) {

        val sessionDescription =
            SessionDescription(
                SessionDescription.Type.ANSWER,
                answer
            )

        peerConnection?.setRemoteDescription(object : SdpObserver {

            override fun onSetSuccess() {
                println("Remote answer set")
            }

            override fun onSetFailure(error: String?) {}

            override fun onCreateSuccess(p0: SessionDescription?) {}

            override fun onCreateFailure(p0: String?) {}

        }, sessionDescription)
    }

    fun addIceCandidate(candidate: IceCandidate) {

        peerConnection?.addIceCandidate(candidate)

    }

    fun closeConnection() {

        println("Closing WebRTC connection")

        audioTrack?.setEnabled(false)
        audioTrack?.dispose()
        audioTrack = null

        peerConnection?.close()
        peerConnection = null

        // create fresh peer connection for next room
        createPeerConnection()
    }

    fun setSpeakerMode(enabled: Boolean){

        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION

        audioManager.isSpeakerphoneOn = enabled

        if(enabled){
            println("Audio routed to SPEAKER")
        }else{
            println("Audio routed to EARPIECE")
        }

    }


}
