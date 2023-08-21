package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.base.BaseFragment
import ai.mawdoo3.salma.base.BaseViewModel
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.databinding.FragmentHelpBinding
import ai.mawdoo3.salma.utils.setNavigationResult
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class HelpFragment : BaseFragment() {
    private val viewModel: ChatBotViewModel by viewModel()
    private val adapter: HelpMessagesAdapter by inject { parametersOf(viewModel) }
    private lateinit var binding: FragmentHelpBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHelpBinding.inflate(inflater, container, false)
        adapter.clear()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = SlideInUpAnimator()
        binding.recyclerView.itemAnimator?.apply {
            addDuration = 400
            removeDuration = 0
            moveDuration = 0
            changeDuration = 0
        }
        viewModel.sendMessage("", "القائمة الرئيسية", false)
        setNavigationResult("Message", "")
        return attachView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.messageResponseList.observe(viewLifecycleOwner) {
            Log.d("SendMessage", "Add Masa message")
            Log.d("GRPC", "Message response")
            adapter.addItems(it)
        }


        viewModel.messageSent.observe(viewLifecycleOwner) {
            Log.d("SendMessage", "Add user message")
            Log.d("GRPC", "Message sent")
            (it as TextMessageUiModel).text?.let { textMessage -> setNavigationResult("Message", textMessage) }
            findNavController().popBackStack()

        }

        viewModel.showLoader.observe(viewLifecycleOwner) {
            adapter.loading(it)
        }

    }

    override fun getViewModel(): BaseViewModel = viewModel


    private fun scrollToBottom() {
        binding.recyclerView.postDelayed(Runnable {
            binding.recyclerView.layoutManager?.smoothScrollToPosition(
                binding.recyclerView,
                RecyclerView.State(), adapter.getListCount() - 1
            );
        }, 500)

    }

}