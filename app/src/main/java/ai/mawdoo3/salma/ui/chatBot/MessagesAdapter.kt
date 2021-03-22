package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.data.dataModel.*
import ai.mawdoo3.salma.databinding.*
import android.view.LayoutInflater
import android.view.ViewGroup
import com.banking.common.base.BaseAdapter
import com.banking.common.base.BaseViewHolder

/**
 * created by Omar Qadomi on 3/17/21
 */
class MessagesAdapter(val viewModel: ChatBotViewModel) :
    BaseAdapter<MessageUiModel, BaseViewHolder<MessageUiModel>>() {
    enum class MessageType(val value: Int) {
        InComingTextMessageType(1),
        OutComingTextMessageType(2),
        QuickRepliesMessageType(3),
        LocationMessageType(4),
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MessageUiModel> {
        when (viewType) {
            MessageType.OutComingTextMessageType.value -> {
                return OutComingTextMessageViewHolder(
                    OutcomingTextMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MessageType.InComingTextMessageType.value -> {
                return InComingTextMessageViewHolder(
                    IncomingTextMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MessageType.QuickRepliesMessageType.value -> {
                return QuickRepliesMessageViewHolder(
                    QuickRepliesMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MessageType.LocationMessageType.value -> {
                return QuickRepliesMessageViewHolder(
                    QuickRepliesMessageItemBinding.inflate(
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
        } else if (list[position] is QuickReplyMessageUiModel) {
            return MessageType.QuickRepliesMessageType.value
        } else if (list[position] is LocationMessageUiModel) {
            return MessageType.LocationMessageType.value
        } else {
            return 0
        }
    }

    inner class InComingTextMessageViewHolder(val binding: IncomingTextMessageItemBinding) :
        BaseViewHolder<MessageUiModel>(binding) {
        override fun bind(position: Int, item: MessageUiModel?) {
            return bind<IncomingTextMessageItemBinding> {
                this.message = item as TextMessageUiModel?
            }
        }
    }

    inner class OutComingTextMessageViewHolder(val binding: OutcomingTextMessageItemBinding) :
        BaseViewHolder<MessageUiModel>(binding) {
        override fun bind(position: Int, item: MessageUiModel?) {
            return bind<OutcomingTextMessageItemBinding> {
                this.message = item as TextMessageUiModel?
            }
        }
    }

    inner class LocationMessageViewHolder(val binding: LocationsMessageItemBinding) :
        BaseViewHolder<MessageUiModel>(binding) {
        override fun bind(position: Int, item: MessageUiModel?) {
            return bind<LocationsMessageItemBinding> {
                val adapter = LocationsAdapter(viewModel)
//                adapter.setItems(list)
                this.recyclerLocations.adapter = adapter
            }
        }
    }

    inner class QuickRepliesMessageViewHolder(val binding: QuickRepliesMessageItemBinding) :
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
}