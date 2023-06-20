@file:Suppress("DEPRECATION")

package com.example.egci428_fortunecookie

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.egci428_fortunecookie.Adapter.CookieAdapter
import com.example.egci428_fortunecookie.Model.Cookie
import com.google.android.material.snackbar.Snackbar



class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var cookieList : ArrayList<Cookie> = ArrayList()
    var cookieAdapter: CookieAdapter = CookieAdapter(cookieList, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        recyclerView = findViewById(R.id.recycleView)
        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = cookieAdapter
        println("=====cookie="+cookieAdapter.itemCount)


        //https://www.geeksforgeeks.org/android-swipe-to-delete-and-undo-in-recyclerview-with-kotlin/
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                println("=====swipe")
                val deletedCourse: Cookie = cookieList[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                cookieList.removeAt(viewHolder.adapterPosition)
                cookieAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(recyclerView, "Deleted " + deletedCourse.name, Snackbar.LENGTH_LONG)//can use Toast without undo action
                    .setAction(
                        "Undo"
                    ) {
                        cookieList.add(position, deletedCourse)
                        cookieAdapter.notifyItemInserted(position)
                    }.show()
            }
        }).attachToRecyclerView(recyclerView)

        button.setOnClickListener{

            val intent = Intent(this,DetailedActivity::class.java)
            startActivityForResult(intent, 0)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

    }
    @Deprecated("Deprecated in Java")
    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("=====ACT DONE")
        if(resultCode == Activity.RESULT_OK){
            println("=====ACT OK")
            val msg = data?.getStringExtra("msg").toString()
            val status = data?.getStringExtra("status").toString()
            val date = data?.getStringExtra("date").toString()
            val owner = data?.getStringExtra("owner").toString()
            val school = data?.getStringExtra("school").toString()
            if(status != "error") {
                cookieList.add(Cookie(msg, status, date, owner, school))
                cookieAdapter.notifyDataSetChanged()
                println("=====ACT ADDED ($msg)")
            }

        }
    }
}