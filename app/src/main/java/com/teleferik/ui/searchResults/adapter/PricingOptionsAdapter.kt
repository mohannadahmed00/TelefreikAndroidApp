package com.teleferik.ui.searchResults.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.R
import com.teleferik.databinding.RowPricingOptionsBinding
import com.teleferik.models.skyscanner.searchResults.PricingOption
import com.teleferik.models.skyscanner.searchResults.SearchResultsResponse


class PricingOptionsAdapter(
    var list: MutableList<PricingOption>,
    private val iClick: OnItemClickListener,
    private val searchData: SearchResultsResponse?
) : RecyclerView.Adapter<PricingOptionsAdapter.ViewHolder>() {


    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowPricingOptionsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(var viewBinding: RowPricingOptionsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: PricingOption) {
            viewBinding.apply {
                bindAgentName(tvAgancyName,data.agents?.first())
                bindPrice(data.price,tvPrice)
                btnChoose.setOnClickListener {
                    iClick.onPriceOptionClicked(data)
                }
            }
        }

        private fun bindAgentName(tvAgancyName: TextView, agentId: Int?) {
            agentId?.let {
                val agentName = searchData?.agents?.find { agent-> agent.id == it }?.name
                agentName?.let {
                    tvAgancyName.text = it
                }

            }

        }
        private fun bindPrice(price: Double?, tvPrice: TextView) {
            price?.let {
                tvPrice.text = viewBinding.root.context.getString(R.string.total_price,it.toString(),searchData?.query?.currency)
            }
        }

    }



    interface OnItemClickListener {
        fun onPriceOptionClicked(item: PricingOption)
    }
}