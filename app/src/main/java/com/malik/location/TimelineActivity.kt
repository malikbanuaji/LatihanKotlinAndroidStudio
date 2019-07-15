package com.malik.location

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import org.json.JSONObject
import java.io.InputStream
import java.lang.Exception

class TimelineActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        val cache = DiskBasedCache(cacheDir, 1024 * 1024)

        val network = BasicNetwork(HurlStack())

        requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        val url = "http://192.168.0.104:5000"

        val stringRequest = JsonObjectRequest(Request.Method.GET, "$url/get_patrol", null,
            Response.Listener { response ->
                Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show()
                Log.d("RESPONSE", response.toString())

                val data = response.getJSONArray("data")
                Log.d("TAG_Timeline", data.toString())
                Log.d("HOME", "$data")
                var listData = ArrayList<User>()
                var adapter: MyUserAdapter? = null

                if (data != null) {
                    for (i in 0 until data.length()) {
                        var e = User()
                        e.name = data.getJSONObject(i).getString("name")
                        e.phone = data.getJSONObject(i).getString("phone")
                        listData.add(e)
                    }
                }

                adapter = MyUserAdapter(this, listData)

                val lv = findViewById<ListView>(R.id.listview1)
                lv.adapter = adapter
                lv.setOnItemClickListener { adapterView, view, i, l ->
                    Toast.makeText(this, listData[i].name, Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error -> Toast.makeText(this, "Error $error", Toast.LENGTH_SHORT).show() }
        )


        requestQueue.add(stringRequest)

//        var obj = JSONObject(loadFromAssets())

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



class User {
    var name: String? = null
    var phone: String? = null
}

class MyUserAdapter(private var activity: TimelineActivity, private var items: ArrayList<User>) : BaseAdapter() {
    private class ViewHolder(row: View?) {
        var lblName: TextView? = null
        var lblPhone: TextView? = null
        init {
            this.lblName = row?.findViewById(R.id.lbl_name)
            this.lblPhone = row?.findViewById(R.id.lbl_phone)
        }
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (p1 === null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.timeline_item, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = p1
            viewHolder = view.tag as ViewHolder
        }

        var user = items[p0]
        viewHolder.lblName?.text = user.name
        viewHolder.lblPhone?.text = user.phone

        return view as View
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}