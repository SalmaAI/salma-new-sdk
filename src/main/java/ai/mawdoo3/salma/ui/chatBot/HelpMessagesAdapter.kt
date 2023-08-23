package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.base.BaseAdapter
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.*
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.data.enums.MessageViewType
import ai.mawdoo3.salma.databinding.*
import ai.mawdoo3.salma.ui.viewHolders.*
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * created by Omar Qadomi on 3/17/21
 */
class HelpMessagesAdapter(val viewModel: ChatBotViewModel) :
    BaseAdapter<MessageUiModel, BaseViewHolder<MessageUiModel>>() {


    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MessageUiModel> {
        when (viewType) {
            MessageViewType.QuickRepliesMessageViewType.value -> {
                return HelpQuickRepliesMessageViewHolder(
                    HelpQuickRepliesMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
//            MessageViewType.InComingTextMessageViewType.value -> {
//                return InComingTextMessageViewHolder(
//                    IncomingTextMessageItemBinding.inflate(
//                        LayoutInflater.from(parent.context),
//                        parent,
//                        false
//                    ), viewModel
//                )
//            }
            else -> {
                return EmptyMessageViewHolder(
                    EmptyMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (position >= list.size) return EnumState.STATE_LOADING.viewType()

        when (list[position]){
            is TextMessageUiModel -> {//text message
                return if (list[position].sender == MessageSender.User) {
                    MessageViewType.OutComingTextMessageViewType.value
                } else {
                    MessageViewType.InComingTextMessageViewType.value
                }
            }
             is QuickReplyMessageUiModel -> {
                return MessageViewType.QuickRepliesMessageViewType.value
            }
            else -> {
                return 0
            }
        }
    }

}