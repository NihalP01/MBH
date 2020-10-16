package com.mridx.c_mbh.Utils

import java.text.DecimalFormat

class Utils {

    companion object {
        const val sliderDelay: Long = 2
        val decimalFormat = DecimalFormat("##.00")
        val qtyList = arrayOf(1, 2, 3, 4, 5)
        const val transition = "Transition"
    }

    enum class CategoryViewType {
        Home, Specific
    }

}