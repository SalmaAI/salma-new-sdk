package ai.mawdoo3.salma.chatBot

import ai.mawdoo3.salma.databinding.FragmentChatBotBinding
import ai.mawdoo3.salma.utils.views.ChatBarView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.banking.common.base.BaseFragment
import com.banking.common.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatBotFragment : BaseFragment(), ChatBarView.ChatBarListener {

    private val viewModel: ChatBotViewModel by viewModel()
    private lateinit var binding: FragmentChatBotBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentChatBotBinding.inflate(inflater, container, false)
        binding.chatActionsView.setActionsListener(this)
        return attachView(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    /**
     * this method will be called when user click send button
     */
    override fun sendMessage(messageText: String) {
        showSnackbarMessage("message sent")
    }


    /**
     * this method will be called when user click on end button while speaking
     */
    override fun onEndSpeaking() {
        TODO("Not yet implemented")
    }
}