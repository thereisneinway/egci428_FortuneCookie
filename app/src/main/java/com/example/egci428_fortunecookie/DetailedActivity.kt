package com.example.egci428_fortunecookie

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import okhttp3.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.lang.ClassCastException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION")
class DetailedActivity : AppCompatActivity() {
    private val num =  kotlin.random.Random.nextInt(0, 9).toString()
    private val jsonURL = "https://egci428-d78f6-default-rtdb.firebaseio.com/opendays/$num.json"
    private var progress = 0
    var message = "null"
    var status = "null"
    var owner = "untitled"
    var school = "n/a"
    private var date = "null"
    private val client = OkHttpClient()
    var ownerText: EditText? = null
    var schoolText: EditText? = null
    @SuppressLint("DiscouragedApi", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(resources.getIdentifier("closed_cookie","drawable",packageName))
        val onScreenText = findViewById<TextView>(R.id.onScreenText)
        val textView4 = findViewById<TextView>(R.id.textView4)
        val textView5 = findViewById<TextView>(R.id.textView5)
        val wishBtn = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        ownerText = findViewById(R.id.ownerText)
        schoolText = findViewById(R.id.schoolText)
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
                        val body = response.body.string()

                        try{
                            val jsonObject = JSONTokener(body).nextValue() as JSONObject
                            message = jsonObject.getString("message")
                            status = jsonObject.getString("status")
                            progress++
                        }catch(e: ClassCastException){
                            message = "%Internet error%"
                            status = "error"
                            progress++
                        }
                    }
                }
            })
        }
        wishBtn.setOnClickListener{
            if(progress == 0) {
                Toast.makeText(this,"YOU HAVE 5 SECONDS", Toast.LENGTH_SHORT).show()
                fetchJson()
                Handler().postDelayed({
                    textView4.text = "Result: $message"
                    onScreenText.text = message
                    date = "Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    textView5.text = date
                    owner = ownerText!!.text.toString()
                    school = schoolText!!.text.toString()
                    ownerText!!.text.clear()
                    schoolText!!.text.clear()
                    wishBtn.text = "Save"
                    imageView.setImageResource(resources.getIdentifier("opened_cookie","drawable",packageName))
                    progress++
                }, 5000)
            }else if(progress > 0){
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra("msg", message)
                intent.putExtra("status", status)
                intent.putExtra("date", date)
                intent.putExtra("owner", owner)
                intent.putExtra("school", school)
                setResult(RESULT_OK,intent)
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                finish()
            }
        }
        button3.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            setResult(Activity.RESULT_CANCELED,intent)
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            finish()
        }

    }
}