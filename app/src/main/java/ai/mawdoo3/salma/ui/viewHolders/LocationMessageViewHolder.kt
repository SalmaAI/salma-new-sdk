package ai.mawdoo3.salma.ui.viewHolders

import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.LocationsListUiModel
import ai.mawdoo3.salma.data.dataModel.MessageUiModel
import ai.mawdoo3.salma.databinding.LocationsMessageItemBinding
import ai.mawdoo3.salma.ui.chatBot.ChatBotViewModel
import ai.mawdoo3.salma.ui.chatBot.LocationsAdapter

class LocationMessageViewHolder(
    val binding: LocationsMessageItemBinding,
    private val viewModel: ChatBotViewModel
) :
    BaseViewHolder<MessageUiModel>(binding) {
    override fun bind(position: Int, item: MessageUiModel?) {
        return bind<LocationsMessageItemBinding> {
            val locations = (item as LocationsListUiModel).locations
            val adapter = LocationsAdapter(viewModel)
            adapter.setItems(locations)
            this.recyclerLocations.adapter = adapter
        }
    }
}