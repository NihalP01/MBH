package com.mridx.c_mbh.Utils

import java.text.DecimalFormat

class Utils {

    companion object {
        const val sliderDelay: Long = 2
        val decimalFormat = DecimalFormat("##.00")
    }

    enum class CategoryViewType {
        Home, Specific
    }

}