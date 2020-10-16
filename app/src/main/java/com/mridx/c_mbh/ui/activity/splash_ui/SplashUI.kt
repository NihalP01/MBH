package com.mridx.c_mbh.ui.activity.splash_ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.mridx.c_mbh.R
import com.mridx.c_mbh.Utils.LiveSession
import com.mridx.c_mbh.Utils.UserPrefManager
import com.mridx.c_mbh.api.ApiHandler
import com.mridx.c_mbh.api.ApiResponseListener
import com.mridx.c_mbh.api.RequestType
import com.mridx.c_mbh.ui.activity.home_ui.HomeUI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class SplashUI : AppCompatActivity(), ApiResponseListener {

    lateinit var userPrefManager: UserPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_ui)


        userPrefManager = UserPrefManager(applicationContext)


        userPrefManager.userIdFlow.asLiveData().observe(this) {
            if (it != -1) {
                GlobalScope.launch {
                    delay(1000 * 2)
                    LiveSession.getInstance().setUserId(it).also {
                        gotoHome()
                    }
                }
            } else {
                fetch()
            }
        }

    }

    private fun gotoHome() {
        startActivity(Intent(this, HomeUI::class.java)).also {
            finish()
        }
    }

    private fun fetch() {
        ApiHandler.instance.apply {
            apiResponseListener = this@SplashUI
        }.also {
            it.fetchUser()
        }
    }

    override fun onResponse(
        success: Boolean,
        type: RequestType,
        responseObj: JSONObject?,
        responseArr: JSONArray?
    ) {
        if (!success) {
            Log.d("kaku", "onResponse: $responseObj")
            return
        }
        when (type) {
            RequestType.GENERATE_USER -> {
                lifecycleScope.launch {
                    userPrefManager.setUserId(responseObj?.getInt("id") ?: -1)
                }
            }
            else -> {
            }
        }
    }

}