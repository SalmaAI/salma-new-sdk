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
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton


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
                val themeWrapper = ContextThemeWrapper(
                    this.root.context,
                    R.style.Theme_Salma_Button_CardButton // my button style
                )

                val actionButton = MaterialButton(themeWrapper)
                actionButton.setBackgroundColor(
                    AppUtils.getColorFromAttr(
                        this.root.context,
                        R.attr.cardButtonBackgroundColor
                    )
                )

                actionButton.text = button.title
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(
                    AppUtils.convertDpToPixel(
                        8f,
                        this.root.context
                    ).toInt(), 0, 0, 0
                )
                actionButton.layoutParams = params
                //set font family
//                val typeface = ResourcesCompat.getFont(this.root.context, R.font.font_medium)
//                actionButton.typeface = typeface
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
                    if (ButtonType.from(button.type) == ButtonType.PhoneNumber) {
                        viewModel.openDialUp.postValue(button.value)
                    } else if (ButtonType.from(button.type) == ButtonType.WebUrl) {
                        viewModel.openLink.postValue(button.value)
                    } else if (ButtonType.from(button.type) == ButtonType.PostBack) {
                        viewModel.sendMessage(text = button.title, payload = button.value)
                    }
                }

                this.linearButtons.addView(actionButton)
                buttonIndex++
            }


        }
    }
}