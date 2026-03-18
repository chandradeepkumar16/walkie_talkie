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
import androidx.compose.ui.text.font.FontWeight
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
    var talking by remember { mutableStateOf(false) }

    val green = Color(0xFF00E676)
    val deviceColor = Color(0xFF1B1F27)
    val background = Color(0xFF0D0F14)

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(background),

        contentAlignment = Alignment.Center

    ){

        Card(

            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(20.dp),

            shape = RoundedCornerShape(24.dp),

            colors = CardDefaults.cardColors(
                containerColor = deviceColor
            )

        ){

            Column(

                modifier = Modifier.padding(25.dp),

                horizontalAlignment = Alignment.CenterHorizontally

            ){

                Text(
                    "CONNECTED",
                    color = green,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(

                    shape = RoundedCornerShape(12.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF111418)
                    )

                ){

                    Text(

                        text = room.replace("room_",""),

                        color = green,

                        fontSize = 42.sp,

                        fontWeight = FontWeight.Bold,

                        modifier = Modifier.padding(
                            horizontal = 45.dp,
                            vertical = 18.dp
                        )

                    )

                }

                Spacer(modifier = Modifier.height(25.dp))

                Row(

                    horizontalArrangement = Arrangement.Center

                ){

                    Button(

                        onClick = {

                            speakerOn = !speakerOn

                            onToggleSpeaker(speakerOn)

                        },

                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if(speakerOn) green else Color.DarkGray
                        )

                    ){

                        if(speakerOn){
                            Text("Speaker")
                        }else{
                            Text("Earpiece")
                        }

                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(

                        onClick = { onPing(room) },

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray
                        )

                    ){

                        Text("Ping")

                    }

                }

                Spacer(modifier = Modifier.height(35.dp))

                Button(

                    onClick = {

                        talking = true

                        onTalk(room)

                    },

                    shape = CircleShape,

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if(talking) Color.Red else green
                    ),

                    modifier = Modifier.size(160.dp)

                ){

                    Text(

                        if(talking) "LIVE" else "TALK",

                        fontSize = 22.sp,

                        fontWeight = FontWeight.Bold

                    )

                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(

                    onClick = {

                        talking = false

                        onStop()

                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )

                ){

                    Text("Stop")

                }

                Spacer(modifier = Modifier.height(30.dp))

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

}