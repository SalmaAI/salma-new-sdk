package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.ItemsListUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.DropdownMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.ui.chatBot.ListBottomSheetFragment
import androidx.appcompat.app.AppCompatActivity

class DropDownMessageViewHolder(
    val binding: DropdownMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<DropdownMessageItemBinding> {
            val listItems = item as ItemsListUiModel?
            binding.tvMessage.text = listItems?.title
            binding.tvMessage.setOnClickListener {
                val listDialog =
                    ListBottomSheetFragment.newInstance(listItems!!.title, listItems!!.items,
                        object : ListBottomSheetFragment.Companion.ListListener {
                            override fun onItemSelected(title: String, payload: String) {
                                viewModel.sendMessage(title, payload)
                            }
                        })
                listDialog.show(
                    (this.root.context as AppCompatActivity).supportFragmentManager,
                    ListBottomSheetFragment.TAG
                )
            }
        }
    }
}