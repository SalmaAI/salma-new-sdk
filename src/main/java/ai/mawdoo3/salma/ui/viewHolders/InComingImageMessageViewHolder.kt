package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.ImageMessageUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.IncomingImageMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel

class InComingImageMessageViewHolder(
    val binding: IncomingImageMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<IncomingImageMessageItemBinding> {
            this.message = item as ImageMessageUiModel?

        }
    }
}