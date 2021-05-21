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
            billMessageItem?.buttons?.forEach { button ->
                val actionButton = TextView(this.root.context)
                actionButton.setText(button.title)
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
                actionButton.setTextColor(
                    ContextCompat.getColor(
                        this.root.context,
                        R.color.masaPrimaryActionColor
                    )
                )
                actionButton.setOnClickListener {
                    if (ButtonType.from(button.type) == ButtonType.PhoneNumber) {
                        viewModel.openDialUp.postValue(button.value)
                    } else if (ButtonType.from(button.type) == ButtonType.WebUrl) {
                        viewModel.openLink.postValue(button.value)
                    } else if (ButtonType.from(button.type) == ButtonType.PostBack) {
                        viewModel.sendMessage(button.value)
                    }
                }
                this.linearButtons.addView(actionButton)
            }


        }
    }
}