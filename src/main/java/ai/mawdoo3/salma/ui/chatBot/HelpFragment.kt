package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.SalmaApplication
import ai.mawdoo3.salma.base.BaseFragment
import ai.mawdoo3.salma.base.BaseViewModel
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.data.dataSource.ChatRemoteDataSource
import ai.mawdoo3.salma.data.dataSource.ChatRepository
import ai.mawdoo3.salma.databinding.FragmentHelpBinding
import ai.mawdoo3.salma.module.RemoteModule
import ai.mawdoo3.salma.utils.setNavigationResult
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.startup.AppInitializer
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class HelpFragment : BaseFragment() {
    private lateinit var viewModel: ChatBotViewModel
    private lateinit var adapter: HelpMessagesAdapter
    private lateinit var binding: FragmentHelpBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHelpBinding.inflate(inflater, container, false)

        val remote = RemoteModule()

        val chatRepo = remote.getAPIServices()?.let { ChatRemoteDataSource(it) }
            ?.let { ChatRepository(it) }

        viewModel = chatRepo?.let { context?.let { it1 ->
            ChatBotViewModel(SalmaApplication(), it,
                it1
            )
        } }!!
        adapter = HelpMessagesAdapter(viewModel)


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
        viewModel = chatRepo?.let { context?.let { it1 ->
            ChatBotViewModel(SalmaApplication(), it,
                it1
            )
        } }!!
        viewModel.sendMessage("", "القائمة الرئيسية", false)
        setNavigationResult("Message", "")
        return attachView(binding.root)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.messageResponseList.observe(viewLifecycleOwner, {
            Log.d("SendMessage", "Add Masa message")
            Log.d("GRPC", "Message response")
            adapter.addItems(it)
        })



        viewModel.messageSent.observe(viewLifecycleOwner, {
            Log.d("SendMessage", "Add user message")
            Log.d("GRPC", "Message sent")
            setNavigationResult("Message", (it as TextMessageUiModel).text!!)
            findNavController().popBackStack()

        })

        viewModel.showLoader.observe(viewLifecycleOwner, {
            adapter.loading(it)
        })

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