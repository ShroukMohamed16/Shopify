package com.example.shopify.address.ui.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.address.model.Address
import com.example.shopify.address.model.GetAddressResponse
import com.example.shopify.databinding.AddressItemBinding
import com.example.shopify.homeFragment.Model.DataCalss.SmartCollection

class AddressAdapter (
    private var AddressList: GetAddressResponse?,
    val context: Context,
    var listener: OnAddressClickListener,
) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    private lateinit var binding: AddressItemBinding

    inner class ViewHolder(var binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AddressItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAddress = AddressList?.addresses?.get(position)
        if(position==0){
            holder.binding.addressItemHomeBtn.visibility=View.VISIBLE
            holder.binding.addressItemDeleteBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("${context?.getString(R.string.homeDeleteAddressMessage)}")
                builder.setPositiveButton("${context?.getString(R.string.ok)}") { dialog, which ->
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }else{

            holder.binding.addressItemHomeBtn.visibility=View.GONE

            holder.binding.addressItemDeleteBtn.setOnClickListener {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("${context?.getString(R.string.deleteAddressTitle)}")
                builder.setMessage("${context?.getString(R.string.deleteAddressMessage)}")
                builder.setPositiveButton("${context?.getString(R.string.yesMapAlert)}") { dialog, which ->
                    if (currentAddress != null) {
                        listener.onAddressDeleteListener(currentAddress.customer_id,currentAddress.id)
                    }
                }
                builder.setNegativeButton("${context?.getString(R.string.CancelMapAlert)}") { dialog, which ->
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }

        }
      holder.binding.addressItemAddressTV.text=
          "${currentAddress?.address1} ${currentAddress?.address2} ${currentAddress?.city} ${currentAddress?.country}"

        holder.binding.addressItemCardView.setOnClickListener {
            listener.onAddressCardDeleteListener(currentAddress!!)
        }
    }

    override fun getItemCount(): Int {
        return AddressList?.addresses!!.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setAddressList(values: List<Address?>?) {
        this.AddressList?.addresses = values as List<Address>
        notifyDataSetChanged()
    }

}