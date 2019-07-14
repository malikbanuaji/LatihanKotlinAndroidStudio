package com.malik.location

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import org.json.JSONObject
import java.io.InputStream
import java.lang.Exception

class NewHomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_home_screen)
    }

    fun onButtonPress(view: View) {
        val intent = Intent(this, TimelineActivity::class.java)
        startActivity(intent)
        var obj = JSONObject(loadFromAssets())
        val data = obj.getJSONArray("data")
        Log.d("HOME", "$data")
    }

    private fun loadFromAssets(): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = application.assets.open("po.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return json
    }
}
