package com.example.shopify.favourite.ui.view

import com.example.shopify.base.line_items

interface OnClickListener {
    fun onClickDeleteIcon(lineItems: line_items)
    fun onClickProductCard(id: Long)

}