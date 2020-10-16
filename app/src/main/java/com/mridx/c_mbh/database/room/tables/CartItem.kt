package com.mridx.c_mbh.database.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
class CartItem {

    companion object {
        const val CART_ITEM = "cart_items"
    }

    @PrimaryKey(autoGenerate = true)
    var id = 0
        private set

    @ColumnInfo(name = "item_id")
    var item_id: String
        private set

    @ColumnInfo(name = "item_name")
    var item_name: String
        private set

    @ColumnInfo(name = "unit")
    var unit: String
        private set

    @ColumnInfo(name = "quantity")
    var quantity: Int = 0
        private set

    @ColumnInfo(name = "price")
    var price: Float
        private set

    @ColumnInfo(name = "image")
    var image: String
        private set

    @ColumnInfo(name = "mrp")
    var mrp: Float
        private set

    @ColumnInfo(name = "type")
    var type: String
        private set

    constructor(
        id: Int,
        item_id: String,
        item_name: String,
        unit: String,
        quantity: Int,
        price: Float,
        image: String,
        mrp: Float,
        type: String
    ) {
        this.id = id
        this.item_id = item_id
        this.item_name = item_name
        this.unit = unit
        this.quantity = quantity
        this.price = price
        this.image = image
        this.mrp = mrp
        this.type = type
    }


    @Ignore
    constructor(
        item_id: String,
        item_name: String,
        unit: String,
        quantity: Int,
        price: Float,
        image: String,
        mrp: Float,
        type: String
    ) {
        this.item_id = item_id
        this.item_name = item_name
        this.unit = unit
        this.quantity = quantity
        this.price = price
        this.image = image
        this.mrp = mrp
        this.type = type
    }


}