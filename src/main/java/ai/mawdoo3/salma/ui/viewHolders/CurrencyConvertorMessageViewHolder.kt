package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.CurrencyMessageUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.IncomingCurrencyConvertorMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel

class CurrencyConvertorMessageViewHolder(
    val binding: IncomingCurrencyConvertorMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<IncomingCurrencyConvertorMessageItemBinding> {
            this.message = item as CurrencyMessageUiModel?
        }
    }
}