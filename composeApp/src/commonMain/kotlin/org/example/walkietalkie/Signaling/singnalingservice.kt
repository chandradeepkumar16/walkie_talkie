package org.example.walkietalkie.signaling

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.delay
import org.example.walkietalkie.model.Signal
import org.example.walkietalkie.model.SignalType
import org.example.walkietalkie.network.SupabaseClient
import kotlin.time.Clock

class SignalingService {

    suspend fun sendSignal(signal: Signal) {
        SupabaseClient.client
            .from("signals")
            .insert(signal)
    }

    suspend fun testInsertSignal() {
        val signal = Signal(
            sender = "android_user",
            room = "room101",
            type = "PING",
            data = "hello from android"
        )

        sendSignal(signal)
        println("Signal sent to Supabase")
    }

    suspend fun listenForSignals(onSignalReceived: (Signal) -> Unit) {
        while (true) {
            try {
                val channel = SupabaseClient.client.channel("signals-channel")

                val changeFlow = channel.postgresChangeFlow<PostgresAction.Insert>(
                    schema = "public"
                ) {
                    this.table = "signals"
                }

                channel.subscribe()

                changeFlow.collect { change ->
                    try {
                        val signal = change.decodeRecord<Signal>()
                        println("Signal received: $signal")
                        onSignalReceived(signal)
                    } catch (e: Exception) {
                        println("Error decoding signal: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                println("Error while listening for messages: ${e.message}. Retrying in 5s...")
                delay(5000)
            }
        }
    }


    suspend fun sendPing(
        room:String,
        sender:String
    ){

        val signal = Signal(

            sender = sender,

            room = room,

            type = SignalType.PING,

            data = "ping"

        )

        sendSignal(signal)

    }



    suspend fun sendOffer(room:String, offer:String){

        val signal = Signal(
            sender = "android_user",
            room = room,
            type = SignalType.OFFER,
            data = offer
        )

        sendSignal(signal)
    }


    suspend fun sendAnswer(room:String, answer:String){

        val signal = Signal(
            sender = "android_user",
            room = room,
            type = SignalType.ANSWER,
            data = answer
        )

        sendSignal(signal)
    }


}
