package ai.mawdoo3.salma.chatBot

import ai.mawdoo3.salma.data.MessageSender
import ai.mawdoo3.salma.data.TextMessageUiModel
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

class ChatBotFragment : BaseFragment(), ChatBarView.ChatBarListener {

    private val viewModel: ChatBotViewModel by viewModel()
    private val adapter: MessagesAdapter by inject()
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
//        binding.recyclerView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
//            // Wait till recycler_view will update itself and then scroll to the end.
//            binding.recyclerView.post {
//                adapter?.itemCount?.takeIf { it > 0 }?.let {
//                    binding.recyclerView.scrollToPosition(it -1)
//                }
//            }
//        }
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    /**
     * this method will be called when user click send button
     */
    override fun sendMessage(messageText: String) {
        adapter.addItem(TextMessageUiModel(messageText, MessageSender.User))
        adapter.loading(true)
        binding.appBar.setExpanded(false)
        binding.recyclerView.post {
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }


    /**
     * this method will be called when user click on end button while speaking
     */
    override fun onEndSpeaking() {
        TODO("Not yet implemented")
    }
}