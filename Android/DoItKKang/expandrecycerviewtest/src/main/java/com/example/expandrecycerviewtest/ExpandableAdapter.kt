package com.example.expandrecycerviewtest

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.expandrecycerviewtest.databinding.ItemRowBinding

class ExpandableAdapter(
    private val personList: List<Person>,
    private val onItemClicked: (Person) -> Unit
) : RecyclerView.Adapter<ExpandableAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ){
            onItemClicked(personList[it])
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(personList[position])
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    inner class MyViewHolder(
        val binding: ItemRowBinding,
        onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        var item: Person? = null // 이걸 해줘야 들어오네1
            private set

        init{
            Log.d("로그1", "$bindingAdapterPosition")
            itemView.setOnClickListener{
                onItemClicked(bindingAdapterPosition)
                Log.d("로그2", "$bindingAdapterPosition")
                Log.d("로그3", "$it")
            }
            binding.imgMore.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
                Log.d("로그4", "들어오나??? $it")
                val show = toggleLayout(!item!!.isExpanded, it, binding.layoutExpand)
                item!!.isExpanded = show
            }
        }
        fun bind(person: Person) {

            item = person // 이걸 해줘야 들어오네2

            binding.txtName.text = person.name
            binding.imgPhoto.setImageResource(person.image)

        }

        private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: LinearLayout): Boolean {
            // 2
            ToggleAnimation.toggleArrow(view, isExpanded)
            if (isExpanded) {
                ToggleAnimation.expand(layoutExpand)
            } else {
                ToggleAnimation.collapse(layoutExpand)
            }
            return isExpanded
        }
    }

}