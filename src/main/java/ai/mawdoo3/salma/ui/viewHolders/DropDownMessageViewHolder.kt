package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.ItemsListUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.DropdownMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.ui.chatBot.ListBottomSheetFragment
import androidx.fragment.app.Fragment

class DropDownMessageViewHolder(
    val binding: DropdownMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<DropdownMessageItemBinding> {
            val listItems = item as ItemsListUiModel?
            binding.root.setOnClickListener {
                ListBottomSheetFragment.newInstance(listItems!!.items).show(
                    (this.root.context as Fragment).childFragmentManager,
                    ListBottomSheetFragment.TAG
                )
            }
        }
    }
}