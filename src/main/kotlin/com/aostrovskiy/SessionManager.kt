package com.aostrovskiy

import com.opentok.OpenTok
import com.opentok.Session

class ParticipantQueue() {
    fun popNext() = Participant(ID("Dsf"))
}

class Engine(
    val queue: ParticipantQueue = ParticipantQueue(),
    val sessionManager: SessionManager = SessionManager()
): ChatEngine {

    override fun nextChat(participant: Participant): Chat {
        val room = sessionManager.createSession()
        val opponent = queue.popNext()
        notifyOpponent(opponent);
        return Chat(listOf(participant, opponent), room)
    }

    fun notifyOpponent(opponent: Participant) {
//        TODO()
    }


}

class SessionManager{
    fun createSession(): Room {
        // inside a class or method...
        val apiKey = 46467122; // YOUR API KEY
        val apiSecret = "5f599d1af6a452083c367f36520df2a29a5ec510";
        val opentok = OpenTok(apiKey, apiSecret);

        val session: Session = opentok.createSession()

// A session that uses the OpenTok Media Router:
        // A session that uses the OpenTok Media Router:
//        val session: Session = OpenTok.createSession(
//            Builder()
//                .mediaMode(MediaMode.ROUTED)
//                .build()
//        )

// A Session with a location hint:
//        // A Session with a location hint:
//        val session: Session = opentok.createSession(
//            Builder()
//                .location("12.34.56.78")
//                .build()
//        )

// A session that is automatically archived (it must used the routed media mode)
        // A session that is automatically archived (it must used the routed media mode)
//        val session: Session = opentok.createSession(
//            Builder()
//                .mediaMode(MediaMode.ROUTED)
//                .archiveMode(ArchiveMode.ALWAYS)
//                .build()
//        )

// Store this sessionId in the database for later use:
        // Store this sessionId in the database for later use:


        // Generate a token from just a sessionId (fetched from a database)
        // Generate a token from just a sessionId (fetched from a database)
        val sessionId: String = session.sessionId
        val token = session.generateToken();
// Generate a token by calling the method on the Session (returned from createSession)
        // Generate a token by calling the method on the Session (returned from createSession)
//        val token = session.generateToken()

// Set some options in a token
        // Set some options in a token
//        val token = session.generateToken(
//            TokenOptions.Builder()
//                .role(Role.MODERATOR)
//                .expireTime(System.currentTimeMillis() / 1000L + 7 * 24 * 60 * 60) // in one week
//                .data("name=Johnny")
//                .build()
//        )
        return Room(sessionId, token)
    }
}