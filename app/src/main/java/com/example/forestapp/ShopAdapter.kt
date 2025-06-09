package com.example.forestapp

import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ShopAdapter(
    private val treeList: List<String>,
    private var userCoins: Int,
    private val treeDrawables: Map<String, Int>,
    private val context: Context,
    private val onPurchaseListener: OnPurchaseListener
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    interface OnPurchaseListener {
        fun onTreePurchased(treeName: String, price: Int)
    }

    fun updateUserCoin(newCoins: Int) {
        userCoins = newCoins
        notifyDataSetChanged()
    }

    inner class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val treeImage: ImageView = view.findViewById(R.id.treeImage)
        val treeName: TextView = view.findViewById(R.id.treeName)
        val treePrice: TextView = view.findViewById(R.id.treePrice)
        val buyButton: TextView = view.findViewById(R.id.buyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val treeName = treeList[position]
        val price = TreeType.getPrice(treeName)

        holder.treeName.text = treeName
        holder.treePrice.text = "$price Coin"
        holder.treeImage.setImageResource(treeDrawables[treeName] ?: R.drawable.beta_balik)

        if (userCoins >= price) {
            holder.buyButton.apply {
                isEnabled = true
                alpha = 1f
                text = "SATIN AL"
                setBackgroundColor(ContextCompat.getColor(context, R.color.purple_200))
                setOnClickListener { onPurchaseListener.onTreePurchased(treeName, price) }
            }
        } else {
            holder.buyButton.apply {
                isEnabled = false
                alpha = 0.5f
                text = "YETERSİZ COIN"
                setBackgroundColor(Color.GRAY)
            }
        }
    }

    override fun getItemCount(): Int = treeList.size
}