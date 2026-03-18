package org.example.walkietalkie.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun JoinRoomScreen(
    onJoinRoom: (String) -> Unit
) {

    var frequency by remember { mutableStateOf("") }

    val green = Color(0xFF00E676)
    val background = Color(0xFF0D0F14)
    val deviceColor = Color(0xFF1B1F27)

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
                    text = "WALKIE TALKIE",
                    color = green,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(25.dp))

                Card(

                    shape = RoundedCornerShape(12.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF111418)
                    )

                ){

                    Text(

                        text =
                            if(frequency.isEmpty())
                                "----"
                            else
                                frequency,

                        color = green,

                        fontSize = 42.sp,

                        fontWeight = FontWeight.Bold,

                        modifier = Modifier.padding(
                            horizontal = 50.dp,
                            vertical = 18.dp
                        )

                    )

                }

                Spacer(modifier = Modifier.height(25.dp))

                OutlinedTextField(

                    value = frequency,

                    onValueChange = {

                        if(it.length <= 6){
                            frequency = it
                        }

                    },

                    label = {
                        Text("Set Frequency")
                    },

                    singleLine = true,

                    modifier = Modifier.fillMaxWidth(),

                    colors = OutlinedTextFieldDefaults.colors(

                        focusedBorderColor = green,

                        unfocusedBorderColor = Color.DarkGray,

                        focusedLabelColor = green,

                        unfocusedLabelColor = Color.Gray,

                        cursorColor = green,

                        focusedTextColor = Color.White,

                        unfocusedTextColor = Color.White,

                        focusedContainerColor = Color(0xFF111418),

                        unfocusedContainerColor = Color(0xFF111418)

                    )

                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(

                    onClick = {

                        if(frequency.isNotEmpty()){

                            val room = "room_$frequency"

                            onJoinRoom(room)

                        }

                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = green
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)

                ){

                    Text(
                        "CONNECT",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Tune frequency to join room",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

            }

        }

    }

}