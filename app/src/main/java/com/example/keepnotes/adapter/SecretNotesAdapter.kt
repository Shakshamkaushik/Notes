package com.example.keepnotes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepnotes.R
import com.example.keepnotes.model.SecretNotes

class SecretNotesAdapter(val context: Context, val listener: iSecretNotes) :
    RecyclerView.Adapter<SecretNotesAdapter.MyViewHolder>() {

    val allSecretNotes = ArrayList<SecretNotes>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtView = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvDesciption = itemView.findViewById<TextView>(R.id.tvDesciption)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        val ivDel = itemView.findViewById<ImageView>(R.id.ivDelete)
        val card = itemView.findViewById<CardView>(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_items, parent, false)
        )
        view.tvDesciption.setOnClickListener {
            // listener.onItemDelClick(allNotes[view.adapterPosition])
        }
        view.card.setOnClickListener {
            listener.onItemEditClick(allSecretNotes[view.adapterPosition])
        }

        view.ivDel.setOnClickListener {
            listener.onItemDelClick(allSecretNotes[view.adapterPosition])
            view.ivDel.visibility = View.GONE
        }
        return view
    }

    override fun getItemCount(): Int {
        return allSecretNotes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = allSecretNotes[position]
        holder.txtView.text = data.title
        holder.tvDate.text = data.date
        holder.tvDesciption.text = data.des
        holder.ivDel.visibility = View.GONE

        holder.txtView.setOnLongClickListener {
            holder.ivDel.visibility = View.VISIBLE
            false
        }

        holder.txtView.setOnClickListener {
            holder.ivDel.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(todoList: ArrayList<SecretNotes>) {
        allSecretNotes.clear()
        allSecretNotes.addAll(todoList)
        notifyDataSetChanged()
    }
}

interface iSecretNotes {

    fun onItemDelClick(secretNotes: SecretNotes)
    fun onItemEditClick(secretNotes: SecretNotes)
}