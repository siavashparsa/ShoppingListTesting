package com.siavashdev.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.siavashdev.shoppinglisttesting.MainCoroutineRule
import com.siavashdev.shoppinglisttesting.getOrAwaitValueTest
import com.siavashdev.shoppinglisttesting.other.Constants
import com.siavashdev.shoppinglisttesting.other.Status
import com.siavashdev.shoppinglisttesting.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp(){
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field , returns error`(){


        viewModel.insertShoppingItem("name","","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert shopping item with too long name , returns error`(){

        val string = buildString {

            for (i in 1..Constants.MAX_NAME_LENGTH + 1){
                append(1)
            }
        }

        viewModel.insertShoppingItem(string,"5","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }
    @Test
    fun `insert shopping item with too long price , returns error`(){

        val string = buildString {

            for (i in 1..Constants.MAX_PRICE_LENGTH + 1){
                append(1)
            }
        }

        viewModel.insertShoppingItem("name","5",string)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert shopping item with too high amount , returns error`(){

        viewModel.insertShoppingItem("name","99999999999999999","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input  , returns success`(){

        viewModel.insertShoppingItem("name","5","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }


}