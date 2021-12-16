package ai.mawdoo3.salma.ui.chatBot

import ai.mawdoo3.salma.MasaSdkInstance
import ai.mawdoo3.salma.R
import ai.mawdoo3.salma.base.BaseFragment
import ai.mawdoo3.salma.base.BaseViewModel
import ai.mawdoo3.salma.data.dataModel.HeaderUiModel
import ai.mawdoo3.salma.data.dataModel.PermissionMessageUiModel
import ai.mawdoo3.salma.data.dataModel.TextMessageUiModel
import ai.mawdoo3.salma.data.enums.ChatBarType
import ai.mawdoo3.salma.data.enums.MessageSender
import ai.mawdoo3.salma.databinding.FragmentChatBotBinding
import ai.mawdoo3.salma.databinding.FragmentHelpBinding
import ai.mawdoo3.salma.ui.GpsUtils
import ai.mawdoo3.salma.utils.AppUtils
import ai.mawdoo3.salma.utils.makeGone
import ai.mawdoo3.salma.utils.setNavigationResult
import ai.mawdoo3.salma.utils.views.ChatBarView
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.assent.GrantResult
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class HelpFragment : BaseFragment() {
    private val viewModel: ChatBotViewModel by viewModel()
    private val adapter: MessagesAdapter by inject { parametersOf(viewModel) }
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
        viewModel.messageResponseList.observe(viewLifecycleOwner, {
            Log.d("SendMessage", "Add Masa message")
            Log.d("GRPC", "Message response")
            adapter.addItems(it)
            scrollToBottom()
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

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }


    private fun scrollToBottom() {
        binding.recyclerView.postDelayed(Runnable {
            binding.recyclerView.layoutManager?.smoothScrollToPosition(
                binding.recyclerView,
                RecyclerView.State(), adapter.getListCount() - 1
            );
        }, 500)

    }

}