package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.base.BaseAdapter
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.CardUiModel
import ai.mawdoo3.salma.data.dataModel.LocationMessageUiModel
import ai.mawdoo3.salma.data.enums.CardTypes
import ai.mawdoo3.salma.databinding.CardItemBinding
import ai.mawdoo3.salma.databinding.LocationItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup


/**
 * created by Omar Qadomi on 27/12/2021
 */
class CardsAdapter(val viewModel: ChatBotViewModel) :
    BaseAdapter<CardUiModel, BaseViewHolder<CardUiModel>>() {

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CardUiModel> {
        return LocationViewHolder(
            CardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class LocationViewHolder(val binding: CardItemBinding) :
        BaseViewHolder<CardUiModel>(binding) {
        override fun bind(
            position: Int,
            item: CardUiModel?
        ) {
            return bind<CardItemBinding> {
                this.card = item
                item?.image?.let {
                    val cardType = CardTypes.from(it)
                    item.name = this.root.context.getString(cardType.nameID)
                    this.imgBackground.setImageResource(cardType.drawableID)
                }
                this.cardRoot.setOnClickListener {
                    viewModel.sendMessage(item!!.cardNumber, item.cardId!!)
                }
            }
        }

    }
}