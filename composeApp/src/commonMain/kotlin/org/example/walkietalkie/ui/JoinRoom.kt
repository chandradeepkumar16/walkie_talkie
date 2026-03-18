package org.example.walkietalkie.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JoinRoomScreen(
    onJoinRoom: (String) -> Unit
) {

    var frequency by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Enter Room Frequency")

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = frequency,
            onValueChange = { frequency = it },
            label = { Text("Room ID (example: 1015)") }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {

            if (frequency.isNotEmpty()) {
                val room = "room_$frequency"
                onJoinRoom(room)
            }

        }) {
            Text("Join Room")
        }
    }
}