package com.keremturker.behero.ui.fragment.donation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.keremturker.behero.base.BaseAdapter
import com.keremturker.behero.base.BaseHolder
import com.keremturker.behero.databinding.ListItemDonationBinding
import com.keremturker.behero.model.Donations
import com.keremturker.behero.utils.extension.formatDate
import com.keremturker.behero.utils.extension.getAddress
import com.keremturker.behero.utils.extension.getBloodImage
import com.keremturker.behero.utils.extension.visibleIf

class DonationsListAdapter(
    private val onClickAction: ((Donations) -> Unit),
    private val onDeleteAction: ((Donations) -> Unit)
) :
    BaseAdapter<Donations, ListItemDonationBinding, DonationsListAdapter.DonationsListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationsListHolder {
        return DonationsListHolder(
            ListItemDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickAction
        )
    }

    inner class DonationsListHolder(
        viewBinding: ListItemDonationBinding,
        private val onClickAction: ((Donations) -> Unit)
    ) :
        BaseHolder<Donations, ListItemDonationBinding>(viewBinding) {
        override fun bind(binding: ListItemDonationBinding, item: Donations?) {

            item?.let { donation ->
                binding.apply {
                    txtName.text = donation.patientName
                    txtLocation.text = donation.address.getAddress()

                    donation.createTime.formatDate()?.let {
                        txtTime.text = it
                    } ?: txtTime.visibleIf(false)

                    parentLayout.setOnClickListener {
                        onClickAction.invoke(donation)
                    }
                    imgBloodGroup.setBackgroundResource(donation.bloodGroup.getBloodImage())
                }
            } ?: return
        }


    }

    fun deleteItem(position: Int) {
        val selectedItem = getData()[position]
        onDeleteAction.invoke(selectedItem)
    }
}

