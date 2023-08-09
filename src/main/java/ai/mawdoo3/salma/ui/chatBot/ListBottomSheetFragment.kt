package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.R.id.design_bottom_sheet
import ai.mawdoo3.salma.base.BaseAdapter
import ai.mawdoo3.salma.data.dataModel.DropdownListItem
import ai.mawdoo3.salma.databinding.ListBottomSheetBinding
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.startup.AppInitializer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * Created on 12/31/20
 */
class ListBottomSheetFragment() : BottomSheetDialogFragment(),
    BaseAdapter.OnItemClickListener<DropdownListItem> {


    private lateinit var binding: ListBottomSheetBinding

    private lateinit var behavior: BottomSheetBehavior<View>

    private lateinit var dialog: BottomSheetDialog
    private lateinit var adapter: ItemsListAdapter
    lateinit var items: List<DropdownListItem>
    lateinit var listener: ListListener
    lateinit var title: String



    companion object {
        val TAG = "ListBottomSheetFragment"
        fun newInstance(
            title: String,
            items: List<DropdownListItem>,
            listener: ListListener
        ): ListBottomSheetFragment {
            val listBottomSheetFragment = ListBottomSheetFragment()
            listBottomSheetFragment.items = items
            listBottomSheetFragment.listener = listener
            listBottomSheetFragment.title = title
            return listBottomSheetFragment
        }

        interface ListListener {
            fun onItemSelected(title: String, payload: String)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListBottomSheetBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ItemsListAdapter()
        adapter.setItems(items)
        binding.recyclerItems.adapter = adapter
        binding.tvTitle.text = title
        binding.etSearch.doAfterTextChanged {
            adapter.filter.filter(it.toString().trim())

        }
        adapter.setOnItemClickListener(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = BottomSheetDialog(requireContext(), R.style.Theme_Salma_BottomSheetDialogTheme);
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet: View? = d.findViewById<View>(design_bottom_sheet)
            behavior = BottomSheetBehavior.from(sheet!!)
            behavior.isHideable = true
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        return dialog
    }

    override fun onItemClicked(view: View, item: DropdownListItem?, position: Int) {
        item?.let { listener.onItemSelected(it.title!!, it.payload!!) }
        dismissAllowingStateLoss()
    }

}