package com.mridx.c_mbh.Utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LiveSession {

    data class SessionData(var cartItem: Int)

    companion object {
        private var _instance: LiveSession? = null
        private lateinit var _sessionData: SessionData
        private var _userId = MutableLiveData<Int>()
        val userId: LiveData<Int> = _userId

        private var _cartItems: MutableLiveData<Int> = MutableLiveData()
        val cartItems: LiveData<Int> = _cartItems

        fun getInstance(): LiveSession {
            if (_instance == null) {
                _sessionData = SessionData(0)
                _instance = LiveSession()
            }
            return _instance as LiveSession
        }

    }

    fun cartItems(i: Int) = _cartItems.postValue(i)

    fun getSessionData(): SessionData = _sessionData

    fun setUserId(id: Int) {
        _userId.postValue(id)
    }

    fun getUserId(): Int = _userId.value ?: -1

}