package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.ButtonUiModel
import ai.mawdoo3.salma.data.dataModel.InformationalMessageUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.data.enums.ButtonFunction
import ai.mawdoo3.salma.data.enums.ButtonType
import ai.mawdoo3.salma.databinding.InformationalMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.utils.AppUtils
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


class InComingInformationalMessageViewHolder(
    val binding: InformationalMessageItemBinding,
    val viewModel: ChatBotViewModel
) : BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<InformationalMessageItemBinding> {
            val message = item as InformationalMessageUiModel?
            this.message = message
            this.linearButtons.removeAllViews()
            var buttonIndex = 0
            message?.buttons?.forEach { button ->
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
                    ).toInt(), AppUtils.convertDpToPixel(
                        16f,
                        this.root.context
                    ).toInt(), 0, 0
                )
                actionButton.layoutParams = params
                //set font family
                val typeface = ResourcesCompat.getFont(this.root.context, R.font.font_medium)
                actionButton.typeface = typeface
                var textColor = ContextCompat.getColor(
                    this.root.context,
                    R.color.masaPrimaryActionColor
                )

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
                        ButtonType.from(button.type) == ButtonType.PerformFunction -> {
                            performFunction(button, this.linearButtons.context)
                        }
                    }
                }

                this.linearButtons.addView(actionButton)
                buttonIndex++
            }

            message?.globalButton?.let { button ->
                when (button.function) {
                    ButtonFunction.Share.value,
                    ButtonFunction.CopyAndShare.value -> {
                        this.imgGlobalButton.setImageDrawable(this.root.context.getDrawable(R.drawable.ic_share))
                    }
                    ButtonFunction.Deeplink.value -> {
                        this.imgGlobalButton.setImageDrawable(this.root.context.getDrawable(R.drawable.ic_open_link))
                    }
                    ButtonFunction.Copy.value -> {
                        this.imgGlobalButton.setImageDrawable(this.root.context.getDrawable(R.drawable.ic_copy))
                    }
                }
                this.imgGlobalButton.setOnClickListener {
                    performFunction(button, this.root.context)
                }
            }

        }
    }

    private fun performFunction(button: ButtonUiModel, context: Context) {
        when (button.function) {
            ButtonFunction.Share.value,
            ButtonFunction.CopyAndShare.value -> {
                AppUtils.openShareIntent(button.value, context)
            }
            ButtonFunction.Deeplink.value -> {
                AppUtils.openLinkInTheBrowser(button.value, context)
            }
            ButtonFunction.Copy.value -> {
                AppUtils.copyToClipboard(button.value, context)
            }
        }
    }
}