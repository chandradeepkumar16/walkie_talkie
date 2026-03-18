package org.example.walkietalkie

import androidx.compose.runtime.*
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.walkietalkie.model.Signal
import org.example.walkietalkie.model.SignalType
import org.example.walkietalkie.signaling.SignalingService
import org.example.walkietalkie.ui.JoinRoomScreen
import org.example.walkietalkie.ui.WalkieTalkieScreen

@Composable
fun App(
    deviceId: String,
    savedRoom: String,
    onSaveRoom: (String) -> Unit,
    onSignalReceived: (Signal) -> Unit,
    onTalk: (String) -> Unit,
    onStop: () -> Unit,
    onExit: () -> Unit,
    onToggleSpeaker: (Boolean) -> Unit
){

    val signalingService = SignalingService()

//    val context = LocalContext.current
//
//    fun playPingSound(){
//
//        val mp = MediaPlayer.create(
//            context,
//            Settings.System.DEFAULT_NOTIFICATION_URI
//        )
//
//        mp.setOnCompletionListener {
//            it.release()
//        }
//
//        mp.start()
//
//    }

    var currentRoom by remember {
        mutableStateOf(savedRoom)
    }

    var isInRoom by remember {
        mutableStateOf(savedRoom.isNotEmpty())
    }

    if (!isInRoom) {

        JoinRoomScreen(

            onJoinRoom = { room ->

                currentRoom = room
                isInRoom = true

                onSaveRoom(room)

            }
        )

    } else {

        WalkieTalkieScreen(

            room = currentRoom,

            onPing = { room ->
                CoroutineScope(Dispatchers.Default).launch {
                    signalingService.sendPing(room,deviceId)
                }
            },

            onTalk = onTalk,
            onStop = onStop,

            onExit = {

                onExit()
                isInRoom = false

            },
            onToggleSpeaker=onToggleSpeaker
        )
    }

    LaunchedEffect(Unit) {

        signalingService.listenForSignals { signal ->

            if (signal.room != currentRoom) return@listenForSignals
            if(!isInRoom) return@listenForSignals
            if(signal.sender == deviceId) return@listenForSignals



            when (signal.type) {

                SignalType.PING -> {
                    println("User wants to talk")
                    onSignalReceived(signal)
                }

                SignalType.OFFER -> {
                    onSignalReceived(signal)
                }

                SignalType.ANSWER -> {
                    onSignalReceived(signal)
                }

            }

        }

    }
}