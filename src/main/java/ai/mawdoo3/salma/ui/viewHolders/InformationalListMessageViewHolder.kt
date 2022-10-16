package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.InformationalListMessageUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.InformationalListMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.ui.chatBot.InformationalListAdapter
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.makeVisible

class InformationalListMessageViewHolder(
    val binding: InformationalListMessageItemBinding,
    private val viewModel: ChatBotViewModel
) :
    BaseViewHolder<MessageUiModel>(binding) {

    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<InformationalListMessageItemBinding> {
            val adapter = InformationalListAdapter(viewModel)
            val items = (item as InformationalListMessageUiModel).items
            if (items.size > 2) {
                btnMore.makeVisible()
                btnMore.text = String.format(
                    binding.btnMore.context.getString(R.string.show_more),
                    items.size - 2
                )
                adapter.setItems(items.subList(0, 2))
            } else {
                adapter.setItems(items)
            }
            btnMore.setOnClickListener {
                btnMore.makeGone()
                adapter.addItems(items.subList(2, items.size))
            }
            this.recyclerInformationalItems.adapter = adapter
            executePendingBindings()
        }
    }
}