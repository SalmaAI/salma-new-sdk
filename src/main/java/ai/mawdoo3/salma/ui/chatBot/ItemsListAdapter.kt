package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.base.BaseFilterableAdapter
import ai.mawdoo3.salma.base.BaseViewHolder
import ai.mawdoo3.salma.data.dataModel.ListItem
import ai.mawdoo3.salma.databinding.ListItemBinding
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.makeVisible
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter


class ItemsListAdapter :
    BaseFilterableAdapter<ListItem, ItemsListAdapter.ViewHolder>() {

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
                if (isLastItem(position)) {
                    this.separator.makeGone()
                } else {
                    this.separator.makeVisible()
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filteredList: MutableList<ListItem>
                if (constraint.isNullOrEmpty()) {
                    filteredList = list
                } else {
                    filteredList =
                        list.filter { item ->
                            item.title?.contains(constraint.toString()) == true
                        } as MutableList<ListItem>
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                setFilteredItems(results?.values as List<ListItem>)
            }

        }
    }

}