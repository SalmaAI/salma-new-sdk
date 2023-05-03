package ai.mawdoo3.salma.base

import ai.mawdoo3.salma.databinding.MasaLoaderRowBinding
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @param M is data model
 * @param V is a ViewHolder
 */
abstract class BaseAdapter<M, V : BaseViewHolder<M>> : RecyclerView.Adapter<V>() {

    internal var currentState: EnumState = EnumState.STATE_NORMAL
    protected var list = mutableListOf<M>()
    var rvHandler: Handler = Handler()
    protected var onItemClickListener: OnItemClickListener<M>? = null


    open fun isEmpty(): Boolean = list.isEmpty()


    open fun isLastItem(position: Int): Boolean = (position == list.size - 1)


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
            if (isLoading && currentState == EnumState.STATE_NORMAL) {
                currentState = EnumState.STATE_LOADING
                notifyItemChanged(list.size)
            } else if (!isLoading && currentState == EnumState.STATE_LOADING) {
                currentState = EnumState.STATE_NORMAL
                removeItem(list.size)
                notifyItemRemoved(list.size)
            } else {
                Log.d("", "")
            }
        }
    }

    fun isLoading(): Boolean = currentState == EnumState.STATE_LOADING


    open fun addItem(item: M) {
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

    fun addItems(items: List<M>, position: Int) {
        rvHandler.post {
            list.addAll(position, items)
            notifyItemRangeInserted(position, items.size - 1)
        }
    }

    open fun removeItem(position: Int) {
        if (list.size > position) {
            rvHandler.post {
                list.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    open fun updateItem(item: M, position: Int) {
        rvHandler.post {
            if (position >= 0 && position < list.size) {
                list.add(position, item)
                notifyItemChanged(position)
            }
        }
    }

    open fun clear() {
        rvHandler.post {
            currentState = EnumState.STATE_NORMAL
            list.clear()
            notifyDataSetChanged()
        }
    }


    open fun getItem(position: Int): M? = if (position >= 0 && position < list.size) {
        list[position]
    } else {
        null
    }


    override fun getItemCount(): Int = if (currentState == EnumState.STATE_LOADING) {
        list.size + 1
    } else {
        list.size
    }


    fun getListCount(): Int = list.size


    override fun getItemViewType(position: Int): Int = if (position >= list.size)
        EnumState.STATE_LOADING.viewType()
    else EnumState.STATE_NORMAL.viewType()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V =
        if (viewType == EnumState.STATE_LOADING.viewType()) {
            LoaderViewHolder(
                MasaLoaderRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ) as (V)
        } else getViewHolder(parent, viewType)


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
    class LoaderViewHolder(binding: MasaLoaderRowBinding) : BaseViewHolder<Nothing>(binding) {
        override fun bind(position: Int, item: Nothing?) = bind<MasaLoaderRowBinding> {
            Log.d("", "$position $item")
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