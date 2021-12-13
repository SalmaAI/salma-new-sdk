package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.base.BaseAdapter
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.ListItem
import ai.mawdoo3.salma.databinding.ListItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup


class ItemsListAdapter :
    BaseAdapter<ListItem, ItemsListAdapter.ViewHolder>() {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(val binding: ListItemBinding) :
        BaseViewHolder<ListItem>(binding) {
        override fun bind(position: Int, item: ListItem?) {
            return bind<ListItemBinding> {
                this.item = item
            }
        }
    }

}