package org.example.walkietalkie

import androidx.compose.runtime.*
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
    savedRoom: String,
    onSaveRoom: (String) -> Unit,
    onSignalReceived: (Signal) -> Unit,
    onTalk: (String) -> Unit,
    onStop: () -> Unit,
    onExit: () -> Unit,
    onToggleSpeaker: (Boolean) -> Unit
){

    val signalingService = SignalingService()

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
                    signalingService.sendPing(room)
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

            when (signal.type) {

                SignalType.PING -> {
                    println("User wants to talk")
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