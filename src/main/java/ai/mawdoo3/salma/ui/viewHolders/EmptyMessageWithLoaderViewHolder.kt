package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.EmptyMessageUiModel
import ai.mawdoo3.salma.data.dataModel.MessageResponse
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.dataModel.QuickReplyMessageUiModel
import ai.mawdoo3.salma.databinding.EmptyMessageItemBinding
import ai.mawdoo3.salma.databinding.EmptyMessageWithLoaderItemBinding
import ai.mawdoo3.salma.databinding.IncomingTextMessageItemBinding
import ai.mawdoo3.salma.databinding.QuickRepliesMessageItemBinding
import ai.mawdoo3.salma.databinding.QuickReplyItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.makeVisible
import ai.mawdoo3.salma.utils.setVisible
import android.view.LayoutInflater

class EmptyMessageWithLoaderViewHolder(val binding: EmptyMessageWithLoaderItemBinding) :
    BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        val isLoading = (item as EmptyMessageUiModel).isLoading
        binding.loading.root.setVisible(isLoading)
    }


}