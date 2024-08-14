package com.toyou.toyouandroid.presentation.fragment.home.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.toyou.toyouandroid.R
import com.toyou.toyouandroid.R.id.limit_200
import com.toyou.toyouandroid.databinding.ItemRvLongEditBinding
import com.toyou.toyouandroid.databinding.ItemRvShortEditBinding
import com.toyou.toyouandroid.model.PreviewCardModel
import com.toyou.toyouandroid.presentation.viewmodel.CardViewModel

class WriteCardAdapter(private val cardViewModel: CardViewModel) : RecyclerView.Adapter<WriteCardAdapter.ViewHolder>() {

    private var cardList : List<PreviewCardModel> = emptyList()

    fun setCards(cards: List<PreviewCardModel>) {
        this.cardList = cards.filter { it.type == 2 }
        //notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRvLongEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    inner class ViewHolder(private val binding : ItemRvLongEditBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(card : PreviewCardModel){
            binding.card = card
            binding.viewModel = cardViewModel

            binding.memoEt.setText(card.answer)

            binding.memoEt.doAfterTextChanged { text ->
                cardViewModel.updateCardAnswer(adapterPosition, text.toString())
            }
            binding.executePendingBindings()

        }
    }

}