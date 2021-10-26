package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.BillsMessageUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.enums.ButtonType
import ai.mawdoo3.salma.databinding.IncomingBillMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.utils.AppUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


class InComingBillMessageViewHolder(
    val binding: IncomingBillMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<IncomingBillMessageItemBinding> {
            val billMessageItem = item as BillsMessageUiModel?
            this.message = billMessageItem
            this.linearButtons.removeAllViews()
            var buttonIndex = 0
            billMessageItem?.buttons?.forEach { button ->
                val actionButton = TextView(this.root.context)
                actionButton.text = button.title
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(
                    AppUtils.convertDpToPixel(
                        16f,
                        this.root.context
                    ).toInt(), 0, 0, 0
                )
                actionButton.setLayoutParams(params)
                //set font family
                val typeface = ResourcesCompat.getFont(this.root.context, R.font.font_medium)
                actionButton.setTypeface(typeface)
                var textColor = ContextCompat.getColor(
                    this.root.context,
                    R.color.masaPrimaryActionColor
                )
                //make last button color as secondary
                if (buttonIndex > 0 && buttonIndex == billMessageItem?.buttons.size - 1) {
                    textColor = ContextCompat.getColor(
                        this.root.context,
                        R.color.masaSecondaryActionColor
                    )
                }
                actionButton.setTextColor(textColor)
                actionButton.setOnClickListener {
                    when {
                        ButtonType.from(button.type) == ButtonType.PhoneNumber -> {
                            viewModel.openDialUp.postValue(button.value)
                        }
                        ButtonType.from(button.type) == ButtonType.WebUrl -> {
                            viewModel.openLink.postValue(button.value)
                        }
                        ButtonType.from(button.type) == ButtonType.PostBack -> {
                            viewModel.sendMessage(text = button.title, payload = button.value)
                        }
                    }
                }

                this.linearButtons.addView(actionButton)
                buttonIndex++
            }


        }
    }
}