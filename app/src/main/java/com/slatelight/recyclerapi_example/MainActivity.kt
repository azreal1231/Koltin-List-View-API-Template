package com.slatelight.recyclerapi_example

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity() {

    var payment_IDs = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val listView: ListView =  listView_payment
        var listView = findViewById<ListView>(R.id.listView_payment)

        val list = mutableListOf<payment_history_Models>()


        fun json2(_jsonarr: JSONArray) {
            print(_jsonarr)

            for (i in 0 until _jsonarr.length()){
                var json_obj = _jsonarr.getJSONObject(i)
                val ref_id = json_obj.getString("merchantReference")
                payment_IDs.add(ref_id)

                val status = json_obj.getString("status")
                val date = json_obj.getString("date")

                val amount = json_obj.getString("totalAmount")
                val amount_len = amount.length
                val rands:String = amount.slice(0 until amount_len-2)     //Gets the rand value
                val cents:String = amount.slice(amount_len-2 until amount_len)   //gets the cents value

                val finalAmount = "R$rands.$cents"    //Creates final amount String



                if (status == "completed"){
                    list.add(payment_history_Models(status, date, finalAmount, R.drawable.checked_green))
                }
                else{
                    list.add(payment_history_Models(status, date, finalAmount, R.drawable.alert_red))
                }
            }

            val adapt = paymentHistoryAdapter(this, R.layout.payment_history_row, list)
            listView.adapter = adapt


            listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                alert(this, "ID: ${payment_IDs[position]}")
            }
        }

        fun read_json(_ctx: Context) {
            val json : String? = null
            try {
                val innerParams = HashMap<String, String>()
                innerParams["WorkflowID"] = ""

                val params = HashMap<String, String>()
                params["de"] = "PrevalidatePYPCollection"


                val jsonObject = JSONObject(params as Map<*, *>)
                val bleh = JSONObject(innerParams as Map<*, *>)
                jsonObject.put("params", bleh)

                val request = JsonObjectRequest(
                    Request.Method.POST, "https://wtfisthis.club/gettransaction", jsonObject,

                    { response ->
                        try {
                            val jsonArray: JSONArray = response.getJSONArray("data")
                            json2(jsonArray)
                        } catch (e: Exception) {
                            alert(this, "Error with BM Server")
                        }

                    }, {
                        alert(this, "Request TimeOut")
                    })
                request.retryPolicy = DefaultRetryPolicy(
                    15000, // TimeOut
                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )

                val contect = _ctx
                VolleySingleton.getInstance(contect).addToRequestQueue(request)

            }catch (e: IOException) {
                println(e)
            }

        }

        read_json(applicationContext)
//        hello
    }
}