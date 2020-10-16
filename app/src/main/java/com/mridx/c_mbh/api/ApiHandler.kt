package com.mridx.c_mbh.api

import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.mridx.c_mbh.Utils.LiveSession
import com.mridx.c_mbh.Utils.VolleySingleton
import org.json.JSONException
import org.json.JSONObject

class ApiHandler {

    var apiResponseListener: ApiResponseListener? = null

    companion object {
        var instance = ApiHandler()
    }

    fun fetchUser() {
        JsonObjectRequest(
            Request.Method.GET,
            Apis.generateUser,
            null,
            { apiResponseListener?.onResponse(true, RequestType.GENERATE_USER, it, null) },
            {
                apiResponseListener?.onResponse(
                    false,
                    RequestType.GENERATE_USER,
                    parseError(it),
                    null
                )
            }
        ).also {
            VolleySingleton.instance?.addToRequestQueue(it)
        }
    }

    fun home() {
        JsonObjectRequest(
            Request.Method.GET,
            Apis.home,
            null,
            { apiResponseListener?.onResponse(true, RequestType.HOME, it, null) },
            {
                apiResponseListener?.onResponse(
                    false,
                    RequestType.HOME,
                    parseError(it),
                    null
                )
            }
        ).also {
            VolleySingleton.instance?.addToRequestQueue(it)
        }
    }

    fun product(id: Int) {
        JsonObjectRequest(
            Request.Method.GET,
            "${Apis.product}$id",
            null,
            { apiResponseListener?.onResponse(true, RequestType.PRODUCT, it, null) },
            {
                apiResponseListener?.onResponse(
                    false,
                    RequestType.PRODUCT,
                    parseError(it),
                    null
                )
            }
        ).also {
            VolleySingleton.instance?.addToRequestQueue(it)
        }
    }

    private fun parseError(it: VolleyError?): JSONObject? {
        if (it!!.networkResponse == null) return null
        try {
            return JSONObject(String(it.networkResponse.data))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null

    }

    fun addToCart(id: Int) {
        JsonObjectRequest(
            Request.Method.POST,
            "${Apis.addToCart}${LiveSession.getInstance().getUserId()}/$id",
            null,
            { apiResponseListener?.onResponse(true, RequestType.ADD_CART, it, null) },
            {
                apiResponseListener?.onResponse(
                    false,
                    RequestType.ADD_CART,
                    parseError(it),
                    null
                )
            }
        ).also {
            VolleySingleton.instance?.addToRequestQueue(it)
        }
    }

}