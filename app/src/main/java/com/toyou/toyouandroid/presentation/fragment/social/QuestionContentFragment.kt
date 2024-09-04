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



        socialViewModel.optionList.observe(viewLifecycleOwner, Observer { options ->
            optionCount = 0
            options?.forEach { optionText ->
                addOption(optionText)
            }
        })

        binding.nextBtn.setOnClickListener {
            socialViewModel.updateOption()
            navController.navigate(R.id.action_questionContentFragment_to_sendFragment)
        }
        binding.backBtn.setOnClickListener {
            socialViewModel.removeOptions()
            socialViewModel.removeContent()
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

        socialViewModel.selectedEmotionMent.observe(viewLifecycleOwner) { ment,  ->
            binding.normalTv.text = ment
        }

        socialViewModel.selectedEmotion.observe(viewLifecycleOwner) { emotion,  ->
            when (emotion) {
                1 -> {
                    binding.balloonTv.setBackgroundResource(R.drawable.balloon_happy)
                    binding.imogeIv.setBackgroundResource(R.drawable.imoge_happy)
                    binding.imageView2.setBackgroundResource(R.drawable.balloon_happy2)
                }
                2 -> {
                    binding.balloonTv.setBackgroundResource(R.drawable.balloon_excited)
                    binding.imogeIv.setBackgroundResource(R.drawable.imoge_excited)
                    binding.imageView2.setBackgroundResource(R.drawable.balloon_excited2)
                }
                3 -> {
                    binding.balloonTv.setBackgroundResource(R.drawable.social_ballon)
                    binding.imogeIv.setBackgroundResource(R.drawable.social_imoge)
                    binding.imageView2.setBackgroundResource(R.drawable.social_balloon)
                }
                4 -> {
                    binding.balloonTv.setBackgroundResource(R.drawable.balloon_anxiety)
                    binding.imogeIv.setBackgroundResource(R.drawable.imoge_anxiety)
                    binding.imageView2.setBackgroundResource(R.drawable.balloon_anxiety2)
                }
                5 -> {
                    binding.balloonTv.setBackgroundResource(R.drawable.balloon_angry)
                    binding.imogeIv.setBackgroundResource(R.drawable.imoge_angry)
                    binding.imageView2.setBackgroundResource(R.drawable.balloon_angry2)
                }
                else -> {
                    binding.balloonTv.setBackgroundResource(R.drawable.balloon_no)
                    binding.imogeIv.setBackgroundResource(0)
                    binding.imageView2.setBackgroundResource(R.drawable.balloon_no2)
                    binding.normalTv.text = "친구가 아직 감정우표를 선택하지 않았어요"
                }
            }
        }


    }

    private fun addOption(optionText: String? = null) {
        if (optionCount < 3) {
            optionCount++

            val optionView = LayoutInflater.from(requireContext()).inflate(R.layout.edittext_with_delete, optionsContainer, false)

            val newOption = optionView.findViewById<EditText>(R.id.edit_text)
            val deleteButton = optionView.findViewById<ImageView>(R.id.delete_button)

            newOption.apply {
                setPadding(10, 30, 5, 30)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                textSize = 12f
                optionText?.let {
                    setText(it)
                }
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


            deleteButton.setOnClickListener {
                val optionsList = socialViewModel.questionDto.value?.options?.toMutableList()
                    ?: mutableListOf()

                val optionText = newOption.text.toString()
                optionsList.remove(optionText)

                socialViewModel.updateQuestionOptions(optionsList)

                optionsContainer.removeView(optionView)
                optionCount--

                if (optionCount < 3) {
                    addOptionButton.isEnabled = true
                }
            }

            optionsContainer.addView(optionView)

            if (optionCount == 3) {
                addOptionButton.isEnabled = false
            }
        }
    }


}