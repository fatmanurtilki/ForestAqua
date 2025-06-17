package com.example.forestapp

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RozetAdapter(
    private val allRozets: List<String>,
    private val earnedRozets: List<String>,
    private val drawables: Map<String, Int>
) : RecyclerView.Adapter<RozetAdapter.RozetViewHolder>() {

    inner class RozetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.rozetImage)
        val name: TextView = view.findViewById(R.id.rozetName)
        val status: TextView = view.findViewById(R.id.rozetStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RozetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rozet, parent, false)
        return RozetViewHolder(view)
    }

    override fun onBindViewHolder(holder: RozetViewHolder, position: Int) {
        val name = allRozets[position]
        val earned = earnedRozets.contains(name)

        holder.name.text = name
        holder.image.setImageResource(drawables[name] ?: R.drawable.rozet_gemi)
        holder.status.text = if (earned) "Kazanıldı" else "Kazanılmadı"
        holder.image.alpha = if (earned) 1f else 0.3f
    }
    override fun getItemCount() = allRozets.size
}
