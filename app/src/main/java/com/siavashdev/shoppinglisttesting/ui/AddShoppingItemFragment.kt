package com.siavashdev.shoppinglisttesting.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.siavashdev.shoppinglisttesting.R
import com.siavashdev.shoppinglisttesting.other.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*
import javax.inject.Inject

class AddShoppingItemFragment @Inject constructor(

    val glide: RequestManager

) : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

        subscribeToObserve()

        btnAddShoppingItem.setOnClickListener {

            viewModel.insertShoppingItem(

                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )


        }

        ivShoppingImage.setOnClickListener {

            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }

        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCurImageUrl("")
                findNavController().popBackStack()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callBack)
    }

    private fun subscribeToObserve() {

        viewModel.curImageUrl.observe(viewLifecycleOwner, Observer {

            glide.load(it).into(ivShoppingImage)

        })

        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner, Observer {

            it.getContentIfNotHandled()?.let { result ->

                when (result.status) {

                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireActivity().rootLayout,
                            "AddedShoppingItem",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireActivity().rootLayout,
                            result.message ?: "An unknown error occured",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        /* NO-OP */
                    }

                }

            }

        })

    }


}