package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.dataModel.QuickReplyMessageUiModel
import ai.mawdoo3.salma.databinding.QuickRepliesMessageItemBinding
import ai.mawdoo3.salma.databinding.QuickReplyItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import android.view.LayoutInflater

class QuickRepliesMessageViewHolder(
    val binding: QuickRepliesMessageItemBinding,
    private val viewModel: ChatBotViewModel
) :
    BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<QuickRepliesMessageItemBinding> {
            this.message = item as QuickReplyMessageUiModel?
            binding.quickRepliesLayout.removeAllViews()
            item?.replies?.forEach { replyElement ->
                binding.quickRepliesLayout.addView(
                    QuickReplyItemBinding.inflate(
                        LayoutInflater.from(this.root.context),
                        null,
                        false
                    ).apply {
                        this.quickReply = replyElement
                        this.root.setOnClickListener {
                            viewModel.sendMessage(replyElement.quickReplyPayload)
                        }
                    }.root
                )
            }
        }
    }
}