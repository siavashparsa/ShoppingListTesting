package com.siavashdev.shoppinglisttesting.repositories

import androidx.lifecycle.LiveData
import com.siavashdev.shoppinglisttesting.data.local.ShoppingItem
import com.siavashdev.shoppinglisttesting.data.remote.responses.ImageResponse
import com.siavashdev.shoppinglisttesting.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}