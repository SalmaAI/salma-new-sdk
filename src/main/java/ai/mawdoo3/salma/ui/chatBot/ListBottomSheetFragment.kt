package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.R.id.design_bottom_sheet
import ai.mawdoo3.salma.base.BaseAdapter
import ai.mawdoo3.salma.data.dataModel.ListItem
import ai.mawdoo3.salma.databinding.ListBottomSheetBinding
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * Created by iSaleem on 12/31/20
 */
class ListBottomSheetFragment() : BottomSheetDialogFragment(),
    BaseAdapter.OnItemClickListener<ListItem> {


    private lateinit var binding: ListBottomSheetBinding

    private lateinit var behavior: BottomSheetBehavior<View>

    private lateinit var dialog: BottomSheetDialog
    private lateinit var adapter: ItemsListAdapter
    lateinit var items: List<ListItem>

    companion object {
        val TAG = "ListBottomSheetFragment"
        fun newInstance(items: List<ListItem>): ListBottomSheetFragment {
            val listBottomSheetFragment = ListBottomSheetFragment()
            listBottomSheetFragment.items = items
            return listBottomSheetFragment
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
//        for (language in Language.values()) {
//            adapter.addItem(
//                LanguageItem(
//                    language.code,
//                    getString(language.titleID),
//                    language.drawableID,
//                    language.code == selectedLanguage
//                )
//            )
//        }
        binding.recyclerLanguages.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet: View? = d.findViewById<View>(design_bottom_sheet)
            behavior = BottomSheetBehavior.from(sheet!!)
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        return dialog
    }

    override fun onItemClicked(view: View, item: ListItem?, position: Int) {

        dismissAllowingStateLoss()
    }

}