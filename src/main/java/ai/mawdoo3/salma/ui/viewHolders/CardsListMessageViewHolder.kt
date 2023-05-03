package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.CardsListMessageUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.CardsListMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.CardsAdapter
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel

/**
 * created by Omar Qadomi on 27/12/2021
 */
class CardsListMessageViewHolder(
    val binding: CardsListMessageItemBinding,
    private val viewModel: ChatBotViewModel
) :
    BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<CardsListMessageItemBinding> {
            val cards = (item as CardsListMessageUiModel).cards
            val adapter = CardsAdapter(viewModel)
            adapter.setItems(cards)
            this.recyclerCards.adapter = adapter
            executePendingBindings()
        }
    }
}