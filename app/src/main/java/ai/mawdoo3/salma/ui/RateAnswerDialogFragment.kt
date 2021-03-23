package ai.mawdoo3.salma.ui

import ai.mawdoo3.salma.RateAnswerDialogListener
import ai.mawdoo3.salma.databinding.RateAnswerDialogBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by Omar Qadomi on 23/3/21
 */
class RateAnswerDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: RateAnswerDialogBinding
    private val args: RateAnswerDialogFragmentArgs by navArgs()
    private lateinit var listener: RateAnswerDialogListener
    private lateinit var answerId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RateAnswerDialogBinding.inflate(inflater, container, false)
        listener = args.listener
        answerId = args.answerID
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgHappy.setOnClickListener {
            listener.rateAnswer(answerId, true)
            dismiss()
        }
        binding.imgSad.setOnClickListener {
            listener.rateAnswer(answerId, false)
            dismiss()
        }

    }
}