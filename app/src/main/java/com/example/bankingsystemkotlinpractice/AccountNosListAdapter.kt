package com.example.bankingsystemkotlinpractice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_layout_list_items_account_no.view.*

import java.util.ArrayList


internal class AccountNosListAdapter(private val context: Context, private val accountNos: ArrayList<String> ) : BaseAdapter() {

    override fun getCount(): Int {
        return accountNos.size
    }

    override fun getItem(position: Int): Any {
        return accountNos[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val accountNo = accountNos[position]
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.activity_layout_list_items_account_no, parent, false)
        }
        val textViewAccountNo = convertView!!.textViewAccountNo
        textViewAccountNo.setText(accountNo)
        return convertView
    }
}
