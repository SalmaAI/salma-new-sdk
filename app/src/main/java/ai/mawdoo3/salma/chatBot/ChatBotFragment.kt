package ai.mawdoo3.salma.chatBot

import ai.mawdoo3.salma.databinding.FragmentChatBotBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.banking.common.base.BaseFragment
import com.banking.common.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatBotFragment : BaseFragment() {

    private val viewModel: ChatBotViewModel by viewModel()
    private lateinit var binding: FragmentChatBotBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentChatBotBinding.inflate(inflater, container, false)
        return attachView(binding.root)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }
}