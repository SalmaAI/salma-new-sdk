package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.databinding.IncomingTextMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.utils.getTextLineCount
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.makeVisible

class InComingTextMessageViewHolder(
    val binding: IncomingTextMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    private val Message_DEFAULT_LINES_SHOWN = 5


    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<IncomingTextMessageItemBinding> {
            this.message = item as TextMessageUiModel?
            binding.tvMessage.maxLines = Message_DEFAULT_LINES_SHOWN
            item?.text.also { description ->
                binding.constraintView.makeVisible()
                binding.tvMessage.text = item?.text
                if (description != null) {
                    binding.tvMessage.getTextLineCount(description) {
                        if (it > Message_DEFAULT_LINES_SHOWN)
                            binding.tvMore.makeVisible()
                        else
                            binding.tvMore.makeGone()
                    }
                }
            } ?: run {
                binding.constraintView.makeGone()
            }
            binding.tvMore.setOnClickListener {
                binding.tvMessage.maxLines = Int.MAX_VALUE
                binding.tvMore.makeGone()
            }

            if (item?.showLocation == true) {
                binding.tvLocation.makeVisible()
            } else {
                binding.tvLocation.makeGone()
            }

            binding.tvRate.setOnClickListener {
                viewModel.rateAnswer.postValue("1")
            }
            binding.tvLocation.setOnClickListener {
                viewModel.getUserLocation.postValue(true)
            }
        }
    }
}