package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.databinding.IncomingTextMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.makeVisible

class InComingTextMessageViewHolder(
    val binding: IncomingTextMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<IncomingTextMessageItemBinding> {
            this.message = item as TextMessageUiModel?
            binding.tvMessage.collapse()
            item?.text?.let {
                if (it.contains("يرجى ارسال موقعك") || it.contains("ابعتلي موقعك")) {
                    binding.tvLocation.makeVisible()
                } else {
                    binding.tvLocation.makeGone()
                }
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