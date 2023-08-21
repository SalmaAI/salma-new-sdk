package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.DropdownListUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.DropdownMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.ui.chatBot.ListBottomSheetFragment
import ai.mawdoo3.salma.utils.disableWithDelay
import androidx.appcompat.app.AppCompatActivity

class DropDownMessageViewHolder(
    val binding: DropdownMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<DropdownMessageItemBinding> {
            val listItems = item as DropdownListUiModel?
            binding.tvMessage.text = listItems?.title
            binding.tvMessage.setOnClickListener {
                it.disableWithDelay()
                val listDialog =
                    listItems?.let { listItms ->
                        ListBottomSheetFragment.newInstance(
                            listItms.title, listItms.items,
                            object : ListBottomSheetFragment.Companion.ListListener {
                                override fun onItemSelected(title: String, payload: String) {
                                    viewModel.sendMessage(title, payload)
                                }
                            })
                    }
                listDialog?.show(
                    (this.root.context as AppCompatActivity).supportFragmentManager,
                    ListBottomSheetFragment.TAG
                )
            }
        }
    }
}