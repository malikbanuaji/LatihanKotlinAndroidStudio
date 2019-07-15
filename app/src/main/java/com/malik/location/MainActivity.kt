package com.malik.location

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val userName: String = "malik"
    val pass: String = "malik"

    private lateinit var requestQueue: RequestQueue

    private lateinit var stringRequest: StringRequest
    private lateinit var stringRequest2: JsonObjectRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun loginOnPress(view: View) {
        val username = findViewById<EditText>(R.id.editTextUsername)
        val password = findViewById<EditText>(R.id.editTextPassword)
        val intent = Intent(this, NewHomeScreen::class.java)
        Log.d("Main Activity", "${username.text}, ${password.text}")

        val url = "http://192.168.0.104:5000"

        val cache = DiskBasedCache(cacheDir, 1024 * 1024)

        val network = BasicNetwork(HurlStack())

        requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show()
                Log.d("RESPONSE", response.toString())
            },
            Response.ErrorListener { error -> Toast.makeText(this, "Error $error", Toast.LENGTH_SHORT).show() }
            )

        val jsonObject = JSONObject()
        jsonObject.put("username", username.text)
        jsonObject.put("password", password.text)


        stringRequest2 = JsonObjectRequest(Request.Method.POST, "$url/auth", JSONObject(jsonObject.toString()),
            Response.Listener { response ->
                Log.d("RESPONSE_POST", response.toString())
                startActivity(intent)
                finish()
        }, Response.ErrorListener { error ->
                Log.d("RESPONSE_ERROR", error.toString())
            })

        stringRequest.tag = "GET_REQUEST"
        stringRequest2.tag= "POST_REQUEST"


        requestQueue.add(stringRequest)
        requestQueue.add(stringRequest2)
    }

    override fun onStop() {
        super.onStop()
        requestQueue.cancelAll("GET_REQUEST")
        requestQueue.cancelAll("POST_REQUEST")
    }

}
