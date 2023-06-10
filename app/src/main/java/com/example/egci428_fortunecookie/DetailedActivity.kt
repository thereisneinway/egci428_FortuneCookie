package com.example.egci428_fortunecookie

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.delay
import okhttp3.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailedActivity : AppCompatActivity() {
    val num =  kotlin.random.Random.nextInt(0, 9).toString()
    private val jsonURL = "https://egci428-d78f6-default-rtdb.firebaseio.com/fortunecookies/"+num+".json"
    private var progress = 0
    var message = "null"
    var status = "null"
    var date = "null"
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        //val toolBar2 = findViewById<Toolbar>(R.id.toolbar2)
        //val toolBar3 = findViewById<Toolbar>(R.id.toolbar3)
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(resources.getIdentifier("closed_cookie","drawable",packageName))
       // val textView2 = findViewById<TextView>(R.id.textView2)
        val onScreenText = findViewById<TextView>(R.id.onScreenText)
        val textView4 = findViewById<TextView>(R.id.textView4)
        val textView5 = findViewById<TextView>(R.id.textView5)
        val wishBtn = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        fun fetchJson() {
            val request = Request.Builder()
                .url(jsonURL)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        for ((name, value) in response.headers) {
                            println("$name: $value")
                        }
                        val body = response.body!!.string()
                        if (body == null) return@use
                        val jsonObject = JSONTokener(body).nextValue() as JSONObject
                        message = jsonObject.getString("message")
                        status = jsonObject.getString("status")
            }}})
        }
        fetchJson()//fetch json before user press button

        wishBtn.setOnClickListener{
            if(progress == 0 && message != "null") {//Before click first time
                textView4.text = "Result: " + message
                onScreenText.text = message
                date = "Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
                textView5.text = date
                wishBtn.text = "Save"
                imageView.setImageResource(resources.getIdentifier("opened_cookie","drawable",packageName))
                progress++
            }else if(progress == 1){//Before click second time
                var intent = Intent(this,MainActivity::class.java)
                intent.putExtra("msg", message)
                intent.putExtra("status", status)
                intent.putExtra("date", date)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish()
            }
        }
        button3.setOnClickListener {
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            finish()
        }
    }
}