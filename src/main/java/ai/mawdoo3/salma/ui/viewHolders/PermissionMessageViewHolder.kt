package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.dataModel.PermissionMessageUiModel
import ai.mawdoo3.salma.databinding.PermissionItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel

class PermissionMessageViewHolder(
    val binding: PermissionItemBinding,
    private val viewModel: ChatBotViewModel
) :
    BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<PermissionItemBinding> {
            this.permission = item as PermissionMessageUiModel
            this.btnSettings.setOnClickListener {
                viewModel.requestPermission.postValue(item.permission)
            }
        }
    }
}