package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.base.BaseAdapter
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.InformationalMessageUiModel
import ai.mawdoo3.salma.data.dataModel.LocationMessageUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.InformationalMessageItemBinding
import ai.mawdoo3.salma.databinding.LocationItemBinding
import ai.mawdoo3.salma.ui.viewHolders.InComingInformationalMessageViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup


/**
 * created by Omar Qadomi on 3/17/21
 */

class InformationalListAdapter(val viewModel: ChatBotViewModel) :
    BaseAdapter<MessageUiModel, BaseViewHolder<MessageUiModel>>() {

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<MessageUiModel> {
        return InComingInformationalMessageViewHolder(
            InformationalMessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), viewModel
        )
    }

}