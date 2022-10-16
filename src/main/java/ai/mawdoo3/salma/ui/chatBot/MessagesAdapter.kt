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
class MessagesAdapter(val viewModel: ChatBotViewModel) :
    BaseAdapter<MessageUiModel, BaseViewHolder<MessageUiModel>>() {


    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MessageUiModel> {
        when (viewType) {
            MessageViewType.HeaderMessageViewType.value -> {
                return HeaderMessageViewHolder(
                    HeaderMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
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
            MessageViewType.PermissionMessageViewType.value -> {
                return PermissionMessageViewHolder(
                    PermissionItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            MessageViewType.InComingBillMessageViewType.value -> {
                return InComingBillMessageViewHolder(
                    IncomingBillMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            MessageViewType.InComingImageMessageViewType.value -> {
                return InComingImageMessageViewHolder(
                    IncomingImageMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            MessageViewType.CurrencyConvertorMessageViewType.value -> {
                return CurrencyConvertorMessageViewHolder(
                    IncomingCurrencyConvertorMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            MessageViewType.InformationalListMessageViewType.value -> {
                return InformationalListMessageViewHolder(
                    InformationalListMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            MessageViewType.DropDownMessageViewType.value -> {
                return DropDownMessageViewHolder(
                    DropdownMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            MessageViewType.ListItemMessageViewType.value -> {
                return ListItemMessageViewHolder(
                    ListMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
            MessageViewType.CardsListMessageViewType.value -> {
                return CardsListMessageViewHolder(
                    CardsListMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), viewModel
                )
            }
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

        when (list[position]) {
            is TextMessageUiModel -> {//text message
                return if (list[position].sender == MessageSender.User) {
                    MessageViewType.OutComingTextMessageViewType.value
                } else {
                    MessageViewType.InComingTextMessageViewType.value
                }
            }
            is HeaderUiModel -> return MessageViewType.HeaderMessageViewType.value
            is QuickReplyMessageUiModel -> return MessageViewType.QuickRepliesMessageViewType.value
            is LocationsListUiModel -> return MessageViewType.LocationMessageViewType.value
            is PermissionMessageUiModel -> return MessageViewType.PermissionMessageViewType.value
            is BillsMessageUiModel -> return MessageViewType.InComingBillMessageViewType.value
            is ImageMessageUiModel -> return MessageViewType.InComingImageMessageViewType.value
            is CurrencyMessageUiModel -> return MessageViewType.CurrencyConvertorMessageViewType.value
            is InformationalListMessageUiModel -> return MessageViewType.InformationalListMessageViewType.value
            is DropdownListUiModel -> return MessageViewType.DropDownMessageViewType.value
            is ListItemMessageUiModel -> return MessageViewType.ListItemMessageViewType.value
            is CardsListMessageUiModel -> return MessageViewType.CardsListMessageViewType.value
            else -> return 0
        }
    }

}