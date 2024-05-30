package com.example.keepnotes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepnotes.R
import com.example.keepnotes.model.Todo


class TodoAdapter(val context: Context, val listener: iTodo) :
    RecyclerView.Adapter<TodoAdapter.MyViewHolder>() {
    val allTodoNotes = ArrayList<Todo>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnLongClickListener, View.OnClickListener {
        val check = itemView.findViewById<CheckBox>(R.id.checkBox)
        val tvTodo = itemView.findViewById<TextView>(R.id.tvTodo)
        val todo_card = itemView.findViewById<CardView>(R.id.card_todo)

        init {
            itemView.setOnLongClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onLongClick(p0: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onLongItemClick(position, allTodoNotes[adapterPosition])
                return true
            }
            return false
        }

        override fun onClick(p0: View?) {
            listener.onItemClick(position, allTodoNotes[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.todo_rv_layout, parent, false)
        )



        return view
    }

    override fun getItemCount(): Int {
        return allTodoNotes.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = allTodoNotes[position]
        holder.tvTodo.text = data.todoTask
        holder.check.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                holder.tvTodo.paintFlags = holder.tvTodo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.tvTodo.setTextColor(Color.GRAY)
            } else {
                holder.tvTodo.paintFlags =
                    holder.tvTodo.paintFlags and (Paint.STRIKE_THRU_TEXT_FLAG.inv())
                holder.tvTodo.setTextColor(Color.WHITE)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(todoList: ArrayList<Todo>) {
        allTodoNotes.clear()
        allTodoNotes.addAll(todoList)
        notifyDataSetChanged()
    }
}

interface iTodo {
    fun onLongItemClick(position: Int, todo: Todo)
    fun onItemClick(position: Int, todo: Todo)
}