package com.toyou.toyouandroid.presentation.fragment.social

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.toyou.toyouandroid.R
import com.toyou.toyouandroid.data.social.dto.request.QuestionDto
import com.toyou.toyouandroid.databinding.FragmentQuestionContentBinding
import com.toyou.toyouandroid.presentation.viewmodel.SocialViewModel

class QuestionContentFragment : Fragment() {
    private var _binding : FragmentQuestionContentBinding? = null
    private val binding : FragmentQuestionContentBinding get() = requireNotNull(_binding){"널"}

    private lateinit var navController: NavController
    private lateinit var socialViewModel : SocialViewModel

    private lateinit var optionsContainer: LinearLayout
    private lateinit var addOptionButton: ImageView
    private var optionCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        socialViewModel = ViewModelProvider(requireActivity()).get(SocialViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionContentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = socialViewModel

        optionsContainer = binding.optionsContainer
        addOptionButton = binding.buttonAddOption

        addOptionButton.setOnClickListener {
            addOption()
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val wordCount: TextView = binding.limit200

        binding.nextBtn.setOnClickListener {
            navController.navigate(R.id.action_questionContentFragment_to_sendFragment)
        }
        binding.backBtn.setOnClickListener {
            navController.popBackStack()
        }

        binding.questionBoxEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                socialViewModel.questionDto.value?.content = s.toString()
                binding.limit200.text = String.format("(%d/50)", s?.length ?: 0)
                Log.d("질문", socialViewModel.questionDto.value.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


    }

    private fun addOption() {
        if (optionCount < 3) {
            optionCount++

            val newOption = EditText(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 10, 0, 10)
                }
                setPadding(10, 30, 5, 30)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                textSize = 12f
                background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.search_container)
            }

            newOption.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val optionsList = socialViewModel.questionDto.value?.options?.toMutableList()
                        ?: mutableListOf()
                    if (optionsList.size >= optionCount) {
                        optionsList[optionCount - 1] = s.toString()
                    } else {
                        optionsList.add(s.toString())
                    }
                    socialViewModel.updateQuestionOptions(optionsList)
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            optionsContainer.addView(newOption)

            if (optionCount == 3) {
                addOptionButton.isEnabled = false
            }
        }
    }

}