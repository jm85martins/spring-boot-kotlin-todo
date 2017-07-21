package com.jm85martins.todokt.entities

/**
 * Created by jorgemartins on 20/07/2017.
 */

data class Item(val name: String, val state: ItemState = ItemState.TODO)

enum class ItemState {
    TODO, DONE
}
