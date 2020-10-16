package com.mridx.c_mbh.api

import org.json.JSONArray
import org.json.JSONObject

interface ApiResponseListener {

    fun onResponse(
        success: Boolean,
        type: RequestType,
        responseObj: JSONObject?,
        responseArr: JSONArray?
    )

}