package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.MessageResponse
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.dataModel.QuickReplyMessageUiModel
import ai.mawdoo3.salma.databinding.HelpQuickRepliesMessageItemBinding
import ai.mawdoo3.salma.databinding.HelpQuickReplyItemBinding
import ai.mawdoo3.salma.databinding.QuickRepliesMessageItemBinding
import ai.mawdoo3.salma.databinding.QuickReplyItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.makeVisible
import android.view.LayoutInflater

class HelpQuickRepliesMessageViewHolder(
    val binding: HelpQuickRepliesMessageItemBinding,
    private val viewModel: ChatBotViewModel
) :
    BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<HelpQuickRepliesMessageItemBinding> {
            item as QuickReplyMessageUiModel?
            binding.containerLayout.removeAllViews()
            item?.replies?.let {
                for (quickReplyElement in item.replies) {
                    var quickReplyItem = HelpQuickReplyItemBinding.inflate(
                        LayoutInflater.from(this.root.context),
                        null,
                        false
                    ).apply {
                        this.quickReply = quickReplyElement
                        this.tvText.setOnClickListener {
                            viewModel.sendMessage(
                                text = quickReplyElement.title,
                                payload = quickReplyElement.quickReplyPayload!!
                            )
                            binding.root.makeGone()
                        }
                    }
                    binding.containerLayout.addView(quickReplyItem.root)
                }
            }
        }
    }

}