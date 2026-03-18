package org.example.walkietalkie

import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.walkietalkie.model.Signal
import org.example.walkietalkie.model.SignalType
import org.example.walkietalkie.signaling.SignalingService
import org.example.walkietalkie.ui.WalkieTalkieScreen

@Composable
fun App(
    onSignalReceived: (Signal) -> Unit,
    onTalk: (String) -> Unit,
    onStop: () -> Unit,
    onExit: () -> Unit
) {

    val signalingService = SignalingService()

    // CURRENT ROOM STATE (dynamic now)
    var currentRoom by remember { mutableStateOf("room_1015") }

    WalkieTalkieScreen(

        onPing = { room ->

            currentRoom = room

            CoroutineScope(Dispatchers.Default).launch {
                signalingService.sendPing(room)
            }

        },

        onTalk = { room ->

            currentRoom = room

            onTalk(room)

        },

        onStop = {
            onStop()
        },

        onExit = {
            onExit()
        }

    )

    LaunchedEffect(Unit) {

        signalingService.listenForSignals { signal ->

            // ONLY ACCEPT SIGNALS FOR CURRENT ROOM
            if(signal.room != currentRoom){
                return@listenForSignals
            }

            when(signal.type){

                SignalType.PING -> {
                    println("User wants to talk")
                }

                SignalType.OFFER -> {
                    println("Offer received")
                    onSignalReceived(signal)
                }

                SignalType.ANSWER -> {
                    println("Answer received")
                    onSignalReceived(signal)
                }

            }

        }

    }

}