package com.keremturker.behero.ui.fragment.donor

import android.view.LayoutInflater
import android.view.ViewGroup
import com.keremturker.behero.base.BaseAdapter
import com.keremturker.behero.base.BaseHolder
import com.keremturker.behero.databinding.ListItemDonorBinding
import com.keremturker.behero.model.Users
import com.keremturker.behero.utils.extension.getAddress
import com.keremturker.behero.utils.extension.getBloodImage

class SearchDonorListAdapter(private val onClickAction: ((Users) -> Unit)) :
    BaseAdapter<Users, ListItemDonorBinding, SearchDonorListAdapter.SearchDonorListHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchDonorListAdapter.SearchDonorListHolder {
        return SearchDonorListHolder(
            ListItemDonorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickAction
        )
    }


    inner class SearchDonorListHolder(
        viewBinding: ListItemDonorBinding,
        private val onClickAction: ((Users) -> Unit)
    ) :
        BaseHolder<Users, ListItemDonorBinding>(viewBinding) {
        override fun bind(binding: ListItemDonorBinding, item: Users?) {

            item?.let { donation ->
                binding.apply {
                    txtName.text = donation.name
                    txtLocation.text = donation.address.getAddress()
                    parentLayout.setOnClickListener {
                        onClickAction.invoke(donation)
                    }
                    imgBloodGroup.setBackgroundResource(donation.bloodGroup.getBloodImage())
                }
            } ?: return
        }
    }
}