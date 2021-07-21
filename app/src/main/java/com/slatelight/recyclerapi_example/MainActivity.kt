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

        getTransactions(this)
    }

    private fun getTransactions(_ctx: Context){
        try {
            val innerParams = HashMap<String, String>()
            innerParams["inner_param"] = "innerParamValue"

            val outerParams = HashMap<String, String>()
            outerParams["outer_param"] = "outerParamValue"


            val outerJsonObject = JSONObject(outerParams as Map<*, *>)
            val innerJsonObject = JSONObject(innerParams as Map<*, *>)
            outerJsonObject.put("params", innerJsonObject)

            val request = JsonObjectRequest(
                Request.Method.POST, "https://wtfisthis.club/gettransaction", outerJsonObject,

                { response ->
                    try {
                        val jsonArray: JSONArray = response.getJSONArray("data")
                        makeRecyclerView(jsonArray, _ctx)
                    } catch (e: Exception) {
                        alert(this, "API Error")
                        print(e)
                    }

                }, {
                    alert(this, "Request TimeOut")
                })
            request.retryPolicy = DefaultRetryPolicy(
                15000, // TimeOut
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            VolleySingleton.getInstance(_ctx).addToRequestQueue(request)

        }catch (e: IOException) {
            println(e)
        }

    }

    private fun makeRecyclerView(_jsonArr: JSONArray, _ctx: Context) {

        val listView = findViewById<ListView>(R.id.listView_payment)

        val list = mutableListOf<payment_history_Models>()

        for (i in 0 until _jsonArr.length()){
            val jsonObj = _jsonArr.getJSONObject(i)
            val refId = jsonObj.getString("merchantReference")

            payment_IDs.add(refId)

            val status = jsonObj.getString("status")
            val date = jsonObj.getString("date")

            val amount = jsonObj.getString("totalAmount")
            val amountLen = amount.length
            val rands:String = amount.slice(0 until amountLen-2)     //Gets the rand value
            val cents:String = amount.slice(amountLen-2 until amountLen)   //gets the cents value

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


}
