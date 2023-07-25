package com.example.shopify.CartFragment.UI.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.address.model.GetAddressResponse
import com.example.shopify.address.ui.view.AddressAdapter
import com.example.shopify.databinding.FragmentCartBinding

class CartFragment : Fragment() , OnCartClickListener  {

    lateinit var cartBinding: FragmentCartBinding
    lateinit var cartAdapter: CartAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var cartRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartBinding = FragmentCartBinding.inflate(inflater, container, false)
        return cartBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter =
            CartAdapter(listOf(), requireContext(), this)

        cartRecyclerView = cartBinding.cartRecycler
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartBinding.checkoutBtn.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_cartFragment_to_paymentFragment)
        }
    }


}