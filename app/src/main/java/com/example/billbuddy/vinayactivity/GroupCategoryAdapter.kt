package com.example.billbuddy.vinayactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R

// Inside GroupCategoryAdapter

// Inside GroupCategoryAdapter

class GroupCategoryAdapter(
    private val categories: List<GroupCategory>,
    private val onItemClick: (GroupCategory) -> Unit
) : RecyclerView.Adapter<GroupCategoryAdapter.ViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        val categoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val itemContainer: LinearLayout = itemView.findViewById(R.id.llItemContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.your_horizontal_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        holder.categoryIcon.setImageResource(category.iconResId)
        holder.categoryName.text = category.categoryName

        // Update background based on the selection
        if (position == selectedPosition) {
            holder.itemContainer.setBackgroundResource(R.drawable.selected_item_background)
        } else {
            holder.itemContainer.setBackgroundResource(R.drawable.rounded_border)
        }

        // Handle item click
        holder.itemView.setOnClickListener {
            // Update the selected position
            selectedPosition = holder.adapterPosition
            // Notify item change to refresh the view
            notifyDataSetChanged()
            // Invoke the onItemClick callback
            onItemClick(category)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}

