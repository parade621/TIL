package com.example.expandrecycerviewtest

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class ExpandableAdapter(
    private val personList: List<Person>,
    private val onItemClicked: (Person) -> Unit
) : RecyclerView.Adapter<ExpandableAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)){
            onItemClicked(personList[it])
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(personList[position])
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    class MyViewHolder(
        itemView: View,
        onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        init{
            Log.d("로그봐라2", "$bindingAdapterPosition")
            itemView.setOnClickListener{
                onItemClicked(bindingAdapterPosition)
                Log.d("로그봐라", "$bindingAdapterPosition")
                Log.d("로그봐라3", "${it.}")
            }
        }
        fun bind(person: Person) {
            val txtName = itemView.findViewById<TextView>(R.id.txt_name)
            val imgPhoto = itemView.findViewById<CircleImageView>(R.id.img_photo)
            val imgMore = itemView.findViewById<ImageButton>(R.id.img_more)
            val layoutExpand = itemView.findViewById<LinearLayout>(R.id.layout_expand)

            txtName.text = person.name
            imgPhoto.setImageResource(person.image)

            imgMore.setOnClickListener {
                // 1
                val show = toggleLayout(!person.isExpanded, it, layoutExpand)
                person.isExpanded = show
            }
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