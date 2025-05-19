package com.example.forestapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forestapp.R

class RozetAdapter(
    private val rozetList: List<String>,
    private val earnedRozets: List<String>,
    private val rozetDrawables: Map<String, Int>
) : RecyclerView.Adapter<RozetAdapter.RozetViewHolder>() {

    inner class RozetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rozetImage: ImageView = itemView.findViewById(R.id.rozetImage)
        val rozetName: TextView = itemView.findViewById(R.id.rozetName)
        val rozetStatus: TextView = itemView.findViewById(R.id.rozetStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RozetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rozet, parent, false)
        return RozetViewHolder(view)
    }

    override fun onBindViewHolder(holder: RozetViewHolder, position: Int) {
        val rozetName = rozetList[position]
        val isEarned = earnedRozets.contains(rozetName)

        holder.rozetName.text = rozetName
        holder.rozetImage.setImageResource(rozetDrawables[rozetName] ?: R.drawable.rozet_gemi)

        if (isEarned) {
            holder.rozetStatus.text = "Kazanıldı"
            holder.rozetImage.alpha = 1f
        } else {
            holder.rozetStatus.text = "Kazanılmadı"
            holder.rozetImage.alpha = 0.3f
        }
    }

    override fun getItemCount() = rozetList.size
}