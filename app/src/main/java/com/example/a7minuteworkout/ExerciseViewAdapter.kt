package com.example.a7minuteworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import  androidx.recyclerview.widget.RecyclerView
import com.example.a7minuteworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(private val exerciseList : ArrayList<ExerciseModel> ) :  RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>()  {

    class ViewHolder(binding:ItemExerciseStatusBinding) :RecyclerView.ViewHolder(binding.root) {
            val tvItem = binding.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : ExerciseModel = exerciseList[position]
        holder.tvItem.text = model.getId().toString()
        when {
            model.getIsSelected() -> {
                holder.tvItem.height = 45
                holder.tvItem.width = 45
                holder.tvItem.setTextColor(Color.WHITE)
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_color_primary_background)
            }
            model.getIsCompleted() -> {
                holder.tvItem.height =25
                holder.tvItem.width = 25
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_color_gray_background)
            }
            else -> {
                holder.tvItem.height = 25
                holder.tvItem.width = 25
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_color_white_background)
            }
        }
    }

    override fun getItemCount(): Int {
        return  exerciseList.size
    }
}