package com.mridx.c_mbh.adapter.callbacks

import android.view.View
import com.dev.aii.adapter.callback.AdapterIdentifier

interface AdapterItemClickedListener {

    fun onClicked(adapterIdentifier: AdapterIdentifier, data: Any)
    fun onClickedWithTransition(adapterIdentifier: AdapterIdentifier, data: Any, sharedView: View)
    fun onLongClicked(adapterIdentifier: AdapterIdentifier, data: Any)

}