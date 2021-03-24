package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.databinding.OutcomingTextMessageItemBinding

class OutComingTextMessageViewHolder(val binding: OutcomingTextMessageItemBinding) :
    BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<OutcomingTextMessageItemBinding> {
            this.message = item as TextMessageUiModel?
        }
    }
}