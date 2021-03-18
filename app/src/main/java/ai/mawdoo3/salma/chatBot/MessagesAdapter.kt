package ai.mawdoo3.salma.chatBot

import ai.mawdoo3.salma.data.MessageSender
import ai.mawdoo3.salma.data.MessageUiModel
import ai.mawdoo3.salma.data.TextMessageUiModel
import ai.mawdoo3.salma.databinding.IncomingTextMessageBinding
import ai.mawdoo3.salma.databinding.OutcomingTextMessageBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.banking.common.base.BaseAdapter
import com.banking.common.base.BaseViewHolder

/**
 * created by Omar Qadomi on 3/17/21
 */
class MessagesAdapter : BaseAdapter<MessageUiModel, BaseViewHolder<MessageUiModel>>() {
    enum class MessageType(val value: Int) {
        InComingTextMessageType(1),
        OutComingTextMessageType(2),
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MessageUiModel> {
        when (viewType) {
            MessageType.OutComingTextMessageType.value -> {
                return OutComingTextMessageViewHolder(
                    OutcomingTextMessageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MessageType.InComingTextMessageType.value -> {
                return InComingTextMessageViewHolder(
                    IncomingTextMessageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
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
                return MessageType.OutComingTextMessageType.value
            } else {
                return MessageType.InComingTextMessageType.value
            }
        } else {
            return 0
        }
    }

    inner class InComingTextMessageViewHolder(val binding: IncomingTextMessageBinding) :
        BaseViewHolder<MessageUiModel>(binding) {
        override fun bind(position: Int, item: MessageUiModel?) {
            return bind<IncomingTextMessageBinding> {
                this.message = item as TextMessageUiModel?
            }
        }
    }

    inner class OutComingTextMessageViewHolder(val binding: OutcomingTextMessageBinding) :
        BaseViewHolder<MessageUiModel>(binding) {
        override fun bind(position: Int, item: MessageUiModel?) {
            return bind<OutcomingTextMessageBinding> {
                this.message = item as TextMessageUiModel?
            }
        }
    }
}