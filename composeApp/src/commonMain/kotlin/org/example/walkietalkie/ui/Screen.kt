package org.example.walkietalkie.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WalkieTalkieScreen(
    onPing: (String) -> Unit,
    onTalk: (String) -> Unit,
    onStop: () -> Unit,
    onExit: () -> Unit
) {

    // frequency state
    var frequency by remember { mutableStateOf("1015") }

    // convert frequency to room
    val currentRoom = "room_$frequency"

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Enter Frequency")

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = frequency,
            onValueChange = { frequency = it },
            label = { Text("Frequency (example: 1015)") }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = { onPing(currentRoom) }) {
            Text("Ping")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { onTalk(currentRoom) }) {
            Text("Tap To Talk")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onStop) {
            Text("Stop")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onExit) {
            Text("Exit")
        }
    }
}