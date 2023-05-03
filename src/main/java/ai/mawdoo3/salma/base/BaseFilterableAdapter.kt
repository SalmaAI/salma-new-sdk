package ai.mawdoo3.salma.base


import android.widget.Filterable

/**
 * @param M is data model
 * @param V is a ViewHolder
 */
abstract class BaseFilterableAdapter<M, V : BaseViewHolder<M>> : BaseAdapter<M, V>(), Filterable {

    var filteredListItems: MutableList<M> = mutableListOf()


    override fun isEmpty(): Boolean {
        return filteredListItems.isEmpty()
    }

    override fun isLastItem(position: Int): Boolean {
        return (position == filteredListItems.size - 1)
    }


    fun setFilteredItems(items: List<M>) {
        rvHandler.post {
            filteredListItems = items as MutableList<M>
            notifyDataSetChanged()
        }
    }

    override fun setItems(items: List<M>) {
        rvHandler.post {
            list.addAll(items)
            filteredListItems.addAll(items)
            notifyItemRangeInserted(/*positionStart*/list.size,/*itemCount*/ items.size)
        }
    }

    fun updateItems(items: List<M>) {
        rvHandler.post {
            list = items as MutableList<M>
            filteredListItems = items
            notifyDataSetChanged()
        }
    }

    override fun addItem(item: M) {
        rvHandler.post {
            list.add(item)
            filteredListItems.add(item)
            notifyItemChanged(filteredListItems.size - 1)
        }
    }

    override fun removeItem(position: Int) {
        rvHandler.post {
            filteredListItems.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    override fun updateItem(item: M, position: Int) {
        rvHandler.post {
            if (position >= 0 && position < filteredListItems.size) {
                filteredListItems.add(position, item)
                notifyItemChanged(position)
            }
        }
    }

    override fun clear() {
        rvHandler.post {
            list.clear()
            filteredListItems.clear()
            notifyDataSetChanged()
        }
    }


    override fun getItem(position: Int): M? {
        return if (position >= 0 && position < filteredListItems.size) {
            filteredListItems[position]
        } else {
            null
        }
    }

    override fun getItemCount(): Int {
        if (currentState == EnumState.STATE_LOADING) {
            return filteredListItems.size + 1
        } else {
            return filteredListItems.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= filteredListItems.size)
            EnumState.STATE_LOADING.viewType()
        else EnumState.STATE_NORMAL.viewType()
    }


    override fun onBindViewHolder(holder: V, position: Int) {
        if (holder is LoaderViewHolder) {
            return
        }
        holder.bind(position, filteredListItems[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClicked(it, getItem(position), position)
        }
    }

}