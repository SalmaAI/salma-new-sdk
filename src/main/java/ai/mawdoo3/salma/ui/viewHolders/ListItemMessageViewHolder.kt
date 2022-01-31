package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.ListItemMessageUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.ListMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel


class ListItemMessageViewHolder(
    val binding: ListMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<ListMessageItemBinding> {
            val message = item as ListItemMessageUiModel?
            this.message = message
            this.rootLayout.setOnClickListener {
                message?.payload?.let {
                    viewModel.sendMessage(message!!.title, message.payload)
                }
            }

        }
    }
}