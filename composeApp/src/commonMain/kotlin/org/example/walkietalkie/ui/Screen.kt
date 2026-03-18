package org.example.walkietalkie.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    val green = Color(0xFF00C853)
    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1115)),

        contentAlignment = Alignment.Center

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(vertical = 30.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(
                text = "CONNECTED",
                color = green,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            Card(

                shape = RoundedCornerShape(12.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1B1F27)
                )

            ){

                Text(

                    text = room.replace("room_",""),

                    color = green,

                    fontSize = 38.sp,

                    modifier = Modifier.padding(
                        horizontal = 50.dp,
                        vertical = 18.dp
                    )

                )

            }

            Spacer(modifier = Modifier.height(30.dp))

            Row {

                Button(

                    onClick = {

                        speakerOn = !speakerOn

                        onToggleSpeaker(speakerOn)

                    }

                ){

                    if(speakerOn){
                        Text("Speaker")
                    }else{
                        Text("Earpiece")
                    }

                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(

                    onClick = { onPing(room) }

                ){

                    Text("Ping")

                }

            }

            Spacer(modifier = Modifier.height(35.dp))

            Button(

                onClick = { onTalk(room) },

                shape = CircleShape,

                colors = ButtonDefaults.buttonColors(
                    containerColor = green
                ),

                modifier = Modifier.size(150.dp)

            ){

                Text(
                    "TALK",
                    fontSize = 20.sp
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(

                onClick = onStop,

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )

            ){

                Text("Stop")

            }

            Spacer(modifier = Modifier.height(35.dp))

            Button(

                onClick = onExit,

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB00020)
                ),

                modifier = Modifier.fillMaxWidth()

            ){

                Text("Exit Room")

            }

        }

    }
}