package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.base.BaseAdapter
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.LocationMessageUiModel
import ai.mawdoo3.salma.databinding.LocationItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * created by Omar Qadomi on 3/17/21
 */
class LocationsAdapter(val viewModel: ChatBotViewModel) :
    BaseAdapter<LocationMessageUiModel, BaseViewHolder<LocationMessageUiModel>>() {

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<LocationMessageUiModel> {
        return LocationViewHolder(
            LocationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class LocationViewHolder(val binding: LocationItemBinding) :
        BaseViewHolder<LocationMessageUiModel>(binding) {
        override fun bind(
            position: Int,
            item: LocationMessageUiModel?
        ) {
            return bind<LocationItemBinding> {
                this.location = item
            }
        }

    }
}