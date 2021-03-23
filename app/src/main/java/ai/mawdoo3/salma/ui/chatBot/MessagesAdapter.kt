package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.data.dataModel.LocationsListUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.dataModel.QuickReplyMessageUiModel
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.data.enums.MessageViewType
import ai.mawdoo3.salma.databinding.IncomingTextMessageItemBinding
import ai.mawdoo3.salma.databinding.LocationsMessageItemBinding
import ai.mawdoo3.salma.databinding.OutcomingTextMessageItemBinding
import ai.mawdoo3.salma.databinding.QuickRepliesMessageItemBinding
import ai.mawdoo3.salma.ui.viewHolders.InComingTextMessageViewHolder
import ai.mawdoo3.salma.ui.viewHolders.LocationMessageViewHolder
import ai.mawdoo3.salma.ui.viewHolders.OutComingTextMessageViewHolder
import ai.mawdoo3.salma.ui.viewHolders.QuickRepliesMessageViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.banking.common.base.BaseAdapter
import com.banking.common.base.BaseViewHolder

/**
 * created by Omar Qadomi on 3/17/21
 */
class MessagesAdapter(val viewModel: ChatBotViewModel) :
    BaseAdapter<MessageUiModel, BaseViewHolder<MessageUiModel>>() {


    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MessageUiModel> {
        when (viewType) {
            MessageViewType.OutComingTextMessageViewType.value -> {
                return OutComingTextMessageViewHolder(
                    OutcomingTextMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MessageViewType.InComingTextMessageViewType.value -> {
                return InComingTextMessageViewHolder(
                    IncomingTextMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            MessageViewType.QuickRepliesMessageViewType.value -> {
                return QuickRepliesMessageViewHolder(
                    QuickRepliesMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            MessageViewType.LocationMessageViewType.value -> {
                return LocationMessageViewHolder(
                    LocationsMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            else -> {
                TODO("Not yet implemented")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position >= list.size)
            return EnumState.STATE_LOADING.viewType()
        else if (list[position] is TextMessageUiModel) {//text message
            if (list[position].sender == MessageSender.User) {
                return MessageViewType.OutComingTextMessageViewType.value
            } else {
                return MessageViewType.InComingTextMessageViewType.value
            }
        } else if (list[position] is QuickReplyMessageUiModel) {
            return MessageViewType.QuickRepliesMessageViewType.value
        } else if (list[position] is LocationsListUiModel) {
            return MessageViewType.LocationMessageViewType.value
        } else {
            return 0
        }
    }

}