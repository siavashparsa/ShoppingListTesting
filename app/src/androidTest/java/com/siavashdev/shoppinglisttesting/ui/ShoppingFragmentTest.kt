package com.siavashdev.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.siavashdev.shoppinglisttesting.R
import com.siavashdev.shoppinglisttesting.adapters.ShoppingItemAdapter
import com.siavashdev.shoppinglisttesting.data.local.ShoppingItem
import com.siavashdev.shoppinglisttesting.data.local.getOrAwaitValue
import com.siavashdev.shoppinglisttesting.launchFragmentInHiltContainer
import com.siavashdev.shoppinglisttesting.repositories.FakeShoppingRepositoryAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var testFragmentFactory:TestShoppingFragmentFactory


    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun swipeShoppingItem_deleteItemInDb(){

        val shoppingItem = ShoppingItem("Test",1,1f,"",1)
        val testViewModel : ShoppingViewModel? =null
        launchFragmentInHiltContainer<ShoppingFragment>(fragmentFactory = testFragmentFactory) {

            viewModel = testViewModel
            viewModel?.insertShoppingItemIntoDb(shoppingItem)
        }

        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                swipeLeft()
            ))
        assertThat(testViewModel?.shoppingItems?.getOrAwaitValue()).isEmpty()
    }


    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ShoppingFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.fabAddShoppingItem)).perform(click())

        verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
        )
    }
}