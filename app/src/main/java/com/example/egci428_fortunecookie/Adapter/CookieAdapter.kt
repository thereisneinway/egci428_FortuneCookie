package com.example.egci428_fortunecookie.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.egci428_fortunecookie.Model.Cookie
import com.example.egci428_fortunecookie.R

class CookieAdapter (private val cookieList: ArrayList<Cookie>,private val context: Context): RecyclerView.Adapter<CookieAdapter.CookieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookieAdapter.CookieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row, parent,false)
        return CookieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CookieViewHolder, position: Int) {
        holder.txtName.text = cookieList[position].name
        if(cookieList[position].status == "positive"){
            holder.txtName.setTextColor(Color.parseColor("#00b4ff"))
        }else{
            holder.txtName.setTextColor(Color.parseColor("#ffaa00"))
        }
        holder.txtDate.text = cookieList[position].date
        holder.txtImage.setImageResource(context.resources.getIdentifier("opened_cookie","drawable",context.packageName))
    }

    override fun getItemCount(): Int {
        return cookieList.size
    }
    class CookieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txtName = itemView.findViewById<TextView>(R.id.txtName)
        var txtDate = itemView.findViewById<TextView>(R.id.txtDate)
        var txtImage = itemView.findViewById<ImageView>(R.id.txtImage)
    }
}
