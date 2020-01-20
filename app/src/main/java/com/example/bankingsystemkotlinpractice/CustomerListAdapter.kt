package com.example.bankingsystemkotlinpractice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_layout_list_of_customers.view.*

import java.util.ArrayList

internal class CustomerListAdapter(context: Context, private val customers: ArrayList<User>) :
    ArrayAdapter<User>(context, 0, customers) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val customer = customers[position]

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_layout_list_of_customers, parent, false)
        }

        val textViewCustomerName = convertView!!.textViewCustomerName
        textViewCustomerName.setText(customer.userName)
        return convertView
    }

}
