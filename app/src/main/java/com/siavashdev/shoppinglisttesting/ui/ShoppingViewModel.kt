package com.siavashdev.shoppinglisttesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siavashdev.shoppinglisttesting.data.local.ShoppingItem
import com.siavashdev.shoppinglisttesting.data.remote.responses.ImageResponse
import com.siavashdev.shoppinglisttesting.other.Constants
import com.siavashdev.shoppinglisttesting.other.Event
import com.siavashdev.shoppinglisttesting.other.Resource
import com.siavashdev.shoppinglisttesting.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(

    private val repository: ShoppingRepository

) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

    fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {

        if (name.isEmpty() && amountString.isEmpty() && priceString.isEmpty()) {

            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "the field must not empty",
                        null
                    )
                )
            )
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {

            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "the name of item must not exceed ${Constants.MAX_NAME_LENGTH} characters",
                        null
                    )
                )
            )

            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {

            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "the price of item must not exceed ${Constants.MAX_PRICE_LENGTH} characters",
                        null
                    )
                )
            )

            return
        }

        val amount  = try {

            amountString.toInt()

        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "please enter a valid number",
                        null
                    )
                )
            )
            return
        }

        val shoppingItem =
            ShoppingItem(name, amount, priceString.toFloat(), _curImageUrl.value ?: "")
        insertShoppingItemIntoDb(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {

        if (imageQuery.isEmpty()){
            return
        }
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {

            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }


    }
}
