package com.mridx.c_mbh.ui.fragment.callbacks

import android.view.View


interface FragmentClickListener {

    fun onClicked(data: Any)
    fun onClicked(data: Any, sharedView: View)


}