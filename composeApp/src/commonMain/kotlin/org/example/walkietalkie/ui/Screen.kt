package org.example.walkietalkie.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WalkieTalkieScreen(

    room: String,

    onPing: (String) -> Unit,
    onTalk: (String) -> Unit,
    onStop: () -> Unit,
    onExit: () -> Unit

) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Connected to $room")

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = { onPing(room) }) {
            Text("Ping")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { onTalk(room) }) {
            Text("Tap To Talk")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onStop) {
            Text("Stop")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onExit) {
            Text("Exit Room")
        }
    }
}