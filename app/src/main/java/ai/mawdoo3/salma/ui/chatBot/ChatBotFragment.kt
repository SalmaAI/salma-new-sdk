package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.data.dataModel.MessageSender
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.databinding.FragmentChatBotBinding
import ai.mawdoo3.salma.utils.views.ChatBarView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.banking.common.base.BaseFragment
import com.banking.common.base.BaseViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatBotFragment : BaseFragment(), ChatBarView.ChatBarListener {

    private val viewModel: ChatBotViewModel by viewModel()
    private val adapter: MessagesAdapter by inject { parametersOf(viewModel) }
    private lateinit var binding: FragmentChatBotBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentChatBotBinding.inflate(inflater, container, false)
        binding.chatActionsView.setActionsListener(this)
        adapter.clear()
        adapter.addItem(TextMessageUiModel("كيف يمكنني مساعدتك؟", MessageSender.Masa))
        binding.recyclerView.adapter = adapter
        return attachView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.messageResponseList.observe(viewLifecycleOwner, {
            adapter.addItems(it)
            binding.recyclerView.postDelayed(Runnable {
                binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
            }, 500)
        })
        viewModel.showLoader.observe(viewLifecycleOwner, {
            binding.appBar.setExpanded(false)
            adapter.loading(it)
        })
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    /**
     * this method will be called when user click send button
     */
    override fun sendMessage(messageText: String) {
        viewModel.sendMessage(messageText)
    }


    /**
     * this method will be called when user click on record button to start speaking
     */
    override fun onStartListening() {

    }


    /**
     * this method will be called when user click on end button while speaking
     */
    override fun onEndListening() {

    }
}