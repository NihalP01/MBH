package com.mridx.c_mbh.Utils

class LiveSession {

    data class SessionData(var cartItem: Int)

    companion object {
        private var _instance: LiveSession? = null
        private lateinit var _sessionData: SessionData

        fun getInstance(): LiveSession {
            if (_instance == null) {
                _sessionData = SessionData(0)
                _instance = LiveSession()
            }
            return _instance as LiveSession
        }


    }

    fun getSessionData(): SessionData = _sessionData

}