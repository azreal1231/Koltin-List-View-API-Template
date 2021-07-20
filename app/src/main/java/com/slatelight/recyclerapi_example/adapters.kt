package com.slatelight.recyclerapi_example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class paymentHistoryAdapter (var mCtx: Context, var resources:Int, var items:List<payment_history_Models>): ArrayAdapter<payment_history_Models>(mCtx, resources, items){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resources, null)

        val imageView: ImageView = view.findViewById(R.id.image)
        val paymentStutus: TextView = view.findViewById(R.id.txt_payment_status)
        val paymentDate: TextView = view.findViewById(R.id.txt_payment_date)
        val paymentAmount: TextView = view.findViewById(R.id.txt_payment_amount)

        var mItem: payment_history_Models = items[position]
        imageView.setImageDrawable(mCtx.resources.getDrawable(mItem.img))
        paymentStutus.text = mItem.status
        paymentDate.text = mItem.date
        paymentAmount.text = mItem.amount

        return view
    }
}
