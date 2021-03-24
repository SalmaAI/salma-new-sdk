package ai.mawdoo3.salma.base

import ai.mawdoo3.salma.databinding.LoaderRowBinding
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @param M is data model
 * @param V is a ViewHolder
 */
abstract class BaseAdapter<M, V : BaseViewHolder<M>> : RecyclerView.Adapter<V>() {

    private var currentState: EnumState = EnumState.STATE_NORMAL
    protected var list = mutableListOf<M>()
    private var rvHandler: Handler = Handler()
    protected var onItemClickListener: OnItemClickListener<M>? = null


    fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    fun isLastItem(position: Int): Boolean {
        return (position == list.size - 1)
    }


/*
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        rvHandler = recyclerView.handler
    }
*/

    open fun setItems(items: List<M>) {
        rvHandler.post {
            list.addAll(items)
            notifyItemRangeInserted(/*positionStart*/list.size,/*itemCount*/ items.size)
        }
    }


    fun loading(isLoading: Boolean) {
        rvHandler.post {
            currentState = if (isLoading) EnumState.STATE_LOADING else EnumState.STATE_NORMAL
            notifyItemChanged(list.size)
        }
    }

    fun addItem(item: M) {
        rvHandler.post {
            list.add(item)
            notifyItemChanged(list.size - 1)
        }
    }

    fun addItems(items: List<M>) {
        rvHandler.post {
            list.addAll(items)
            notifyItemRangeChanged(list.size - items.size - 1, list.size - 1)
        }
    }

    fun removeItem(position: Int) {
        rvHandler.post {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateItem(item: M, position: Int) {
        rvHandler.post {
            if (position >= 0 && position < list.size) {
                list.add(position, item)
                notifyItemChanged(position)
            }
        }
    }

    fun clear() {
        rvHandler.post {
            list.clear()
            notifyDataSetChanged()
        }
    }

    fun getItem(position: Int): M? {
        return if (position >= 0 && position < list.size) {
            list[position]
        } else {
            null
        }
    }

    override fun getItemCount(): Int {
        if (currentState == EnumState.STATE_LOADING) {
            return list.size + 1
        } else {
            return list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= list.size)
            EnumState.STATE_LOADING.viewType()
        else EnumState.STATE_NORMAL.viewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        return if (viewType == EnumState.STATE_LOADING.viewType()) {
            LoaderViewHolder(
                LoaderRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ) as (V)
        } else getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        if (holder is LoaderViewHolder) {
            return
        }

        holder.bind(position, list[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClicked(it, getItem(position), position)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<M>): BaseAdapter<M, V>? {
        this.onItemClickListener = onItemClickListener
        return this
    }

    protected abstract fun getViewHolder(parent: ViewGroup, viewType: Int): V

    interface OnItemClickListener<M> {
        fun onItemClicked(view: View, item: M?, position: Int)
    }

    //todo should be inner class
    class LoaderViewHolder(binding: LoaderRowBinding) : BaseViewHolder<Nothing>(binding) {
        override fun bind(position: Int, item: Nothing?) = bind<LoaderRowBinding> {
        }
    }

    enum class TransactionTypes {
        REPLACE_ALL,
        MODIFY,
        INSERT_ITEM,
        REMOVE_ITEM
    }

    internal enum class EnumState {
        STATE_LOADING {
            override fun viewType() = -2
        },

        STATE_NORMAL {
            override fun viewType() = -1
        };

        abstract fun viewType(): Int
    }
}