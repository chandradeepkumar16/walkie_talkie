package org.example.walkietalkie.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WalkieTalkieScreen(

    room: String,

    onPing: (String) -> Unit,
    onTalk: (String) -> Unit,
    onStop: () -> Unit,
    onExit: () -> Unit,
    onToggleSpeaker: (Boolean) -> Unit

) {

    var speakerOn by remember { mutableStateOf(false) }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        Text(
            text = "ROOM ${room.uppercase()}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { onPing(room) },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Ping")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { onTalk(room) },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("TALK")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = onStop,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("STOP")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(

            onClick = {

                speakerOn = !speakerOn

                onToggleSpeaker(speakerOn)

            },

            modifier = Modifier.fillMaxWidth()

        ){

            if(speakerOn){

                Text("Speaker Mode ON")

            }else{

                Text("Earpiece Mode")

            }

        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(

            onClick = onExit,

            modifier = Modifier.fillMaxWidth()

        ){
            Text("Exit Room")
        }

    }
}