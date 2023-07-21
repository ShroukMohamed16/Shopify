package com.example.shopify.setting.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.CurrencyItemBinding

class CurrencyAdapter(private var currencyList: List<List<String>>, var listner: OnClickCurrency) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    private lateinit var binding: CurrencyItemBinding

    inner class CurrencyViewHolder(var binding: CurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CurrencyItemBinding.inflate(inflater, parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencyList[position]
        holder.binding.currencyName.text = " ${currency[1]}"
        holder.binding.currencyCode.text = "${currency[0]} "
        holder.binding.currencyItem.setOnClickListener {
            listner.changeCurrency(currencyList[holder.adapterPosition][0])
        }
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    fun setCurrencyList(currencyList: List<List<String>>) {
        this.currencyList = currencyList
        notifyDataSetChanged()
    }
}