package com.example.forestapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ShopAdapter(
    private val treeList: List<String>,
    private val user: User,  // Artık tüm User objesini alıyoruz
    private val treeDrawables: Map<String, Int>,
    private val context: Context,
    private val onPurchaseListener: OnPurchaseListener
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    interface OnPurchaseListener {
        fun onTreePurchased(treeName: String, price: Int)
    }

    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val treeImage: ImageView = itemView.findViewById(R.id.treeImage)
        val treeName: TextView = itemView.findViewById(R.id.treeName)
        val treePrice: TextView = itemView.findViewById(R.id.treePrice)
        val buyButton: TextView = itemView.findViewById(R.id.buyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val treeName = treeList[position]
        val treePrice = TreeType.getPrice(treeName)

        holder.treeName.text = treeName
        holder.treePrice.text = "$treePrice Coin"
        holder.treeImage.setImageResource(treeDrawables[treeName] ?: R.drawable.beta_balik)

        // Coin kontrolü doğrudan User objesinden
        if (user.coins >= treePrice) {
            holder.buyButton.isEnabled = true
            holder.buyButton.alpha = 1f
            holder.buyButton.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_200))
            holder.buyButton.text = "SATIN AL"
        } else {
            holder.buyButton.isEnabled = false
            holder.buyButton.alpha = 0.5f
            holder.buyButton.setBackgroundColor(Color.GRAY)
            holder.buyButton.text = "YETERSİZ COIN"
        }

        holder.buyButton.setOnClickListener {
            if (user.coins >= treePrice) {
                onPurchaseListener.onTreePurchased(treeName, treePrice)

                // UI güncellemeleri
                holder.buyButton.isEnabled = false
                holder.buyButton.text = "SATIN ALINDI"
                holder.buyButton.setBackgroundColor(Color.GRAY)


            }
        }
    }
    override fun getItemCount(): Int {
        return treeList.size
    }
}

