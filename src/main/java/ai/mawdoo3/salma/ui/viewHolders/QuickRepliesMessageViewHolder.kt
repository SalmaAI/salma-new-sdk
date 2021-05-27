package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.MessageResponse
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.dataModel.QuickReplyMessageUiModel
import ai.mawdoo3.salma.databinding.QuickRepliesMessageItemBinding
import ai.mawdoo3.salma.databinding.QuickReplyItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.makeVisible
import android.view.LayoutInflater

class QuickRepliesMessageViewHolder(
    val binding: QuickRepliesMessageItemBinding,
    private val viewModel: ChatBotViewModel
) :
    BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<QuickRepliesMessageItemBinding> {
            binding.root.makeVisible()
            this.message = item as QuickReplyMessageUiModel?
            binding.quickRepliesLayout.removeAllViews()
            var totalRepliesChar = 0
            var quickReplyIndex = 0
            item?.replies?.let {
                for (quickReplyElement in item.replies) {
                    var quickReplyItem = QuickReplyItemBinding.inflate(
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
                    binding.quickRepliesLayout.addView(quickReplyItem.root)
                    quickReplyElement.title?.let {
                        totalRepliesChar += quickReplyElement.title.length
                    }
                    if (totalRepliesChar >= 100 && item.replies.size > quickReplyIndex + 1) {
                        quickReplyItem.tvMore.makeVisible()
                        quickReplyItem.tvMore.setOnClickListener {
                            it.makeGone()
                            loadMoreOptions(
                                item.replies.subList(
                                    quickReplyIndex + 1,
                                    item.replies.size - 1
                                )
                            )
                        }
                        break
                    }
                    quickReplyIndex++
                }
            }
        }
    }

    private fun loadMoreOptions(
        subList: List<MessageResponse.MessageContentResponse.Element>
    ) {
        subList.forEach { quickReplyElement ->
            binding.quickRepliesLayout.addView(
                QuickReplyItemBinding.inflate(
                    LayoutInflater.from(binding.quickRepliesLayout.context),
                    null,
                    false
                ).apply {
                    this.quickReply = quickReplyElement
                    this.root.setOnClickListener {
                        viewModel.sendMessage(
                            text = quickReplyElement.title,
                            payload = quickReplyElement.quickReplyPayload!!
                        )
                    }
                }.root
            )
        }
    }
}