package com.aostrovskiy

import com.fasterxml.jackson.databind.SerializationFeature
import com.opentok.Session
import io.ktor.http.ContentType
import io.ktor.application.*
import io.ktor.features.AutoHeadResponse
import io.ktor.features.AutoHeadResponse.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.*
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.text.DateFormat

inline class ID(val value: String)
data class Room(val sessionId: String, val token: String)
data class Participant(val id: ID)
data class Chat(val participants: List<Participant>, val room: Room)

interface ChatEngine {
    fun nextChat(participant: Participant) : Chat
}



val currentParticipant = Participant(ID("1234"))

fun main(args: Array<String>) {
    val sessionManager = SessionManager()
    val engine = Engine();



    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
                dateFormat = DateFormat.getDateInstance()
            }
        }

        routing {
            get("/") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }
            get("/next") {
                val nextChat = engine.nextChat(currentParticipant)
                call.respond(nextChat)
            }
        }
    }
    server.start(wait = true)
}

