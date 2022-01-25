package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.HeaderUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.HeaderMessageItemBinding
import java.util.*

class HeaderMessageViewHolder(
    val binding: HeaderMessageItemBinding,
) : BaseViewHolder<MessageUiModel>(binding) {

    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<HeaderMessageItemBinding> {
            val item = item as HeaderUiModel?
            this.name = item?.name

            var welcomeImage: Int
            val hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            when (hours) {
                in 5..11 -> {
                    welcomeImage = R.drawable.header_morning
                }
                in 12..17 -> {
                    welcomeImage = R.drawable.header_evening
                }
                else -> {
                    welcomeImage = R.drawable.header_night
                }
            }
            binding.imgHeader.setImageResource(welcomeImage)

        }
    }
}