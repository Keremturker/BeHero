package com.keremturker.behero.ui.fragment.donation.mine

import android.view.LayoutInflater
import android.view.ViewGroup
import com.keremturker.behero.base.BaseAdapter
import com.keremturker.behero.base.BaseHolder
import com.keremturker.behero.databinding.ListItemDonationBinding
import com.keremturker.behero.model.Donations

class MineDonationsListAdapter(private val onClickAction: ((Donations) -> Unit)) :
    BaseAdapter<Donations, ListItemDonationBinding, MineDonationsListAdapter.MineDonationsListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MineDonationsListHolder {
        return MineDonationsListHolder(
            ListItemDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickAction
        )
    }

    inner class MineDonationsListHolder(
        viewBinding: ListItemDonationBinding,
        private val onClickAction: ((Donations) -> Unit)
    ) :
        BaseHolder<Donations, ListItemDonationBinding>(viewBinding) {
        override fun bind(binding: ListItemDonationBinding, items: Donations?) {
            binding.apply {
                items?.let {item->
                    txtAddress.text = item.uuid
                    txtAddress.setOnClickListener { onClickAction.invoke(item) }
                }
            }
        }
    }
}

