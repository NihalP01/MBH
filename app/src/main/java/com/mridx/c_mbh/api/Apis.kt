package com.mridx.c_mbh.api

class Apis {

    companion object {
        private const val base = "http://13.233.114.56/api/"
        const val image = "http://13.233.114.56/storage/"

        const val generateUser = "${base}generate-user-id"
        const val home = "${base}home"
        const val product = "${base}product/"
        const val addToCart = "${base}add-cart/"
    }

}