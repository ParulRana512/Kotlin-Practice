package com.example.bankingsystemkotlinpractice

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import java.util.ArrayList

class ManagerDashBoard : BaseActivity() {

    companion object {
        const val CUSTOMER_LIST_REQUEST_CODE = 1003
    }

    private var listViewCustomers: ListView? = null
    private var buttonTransfer: Button? = null
    private var customers: ArrayList<User>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_dash_board)

        showCustomers()

        registerHandlers()

    }

    private fun showCustomers() {
        customers!!.clear()
        customers = User.getCustomers(this)
        if (customers == null) {
            showToast(getString(R.string.customer_not_available))
        } else {
            val customerListAdapter = CustomerListAdapter(this, customers!!)
            listViewCustomers!!.adapter = customerListAdapter
            listViewItemClickHandler()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (customers != null) {
            customers!!.clear()
        }
        setResult(RESULT_OK)
        finish()
    }

    private fun listViewItemClickHandler() {
        listViewCustomers!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val customer = listViewCustomers!!.getItemAtPosition(position) as User
                showCustomerDetails(customer.userId)
            }
    }

    private fun showCustomerDetails(customerId: Int) {
        val customerDetailIntent = Intent(this, CustomerDetailActivity::class.java)
        customerDetailIntent.putExtra("CUSTOMER_ID", customerId)
        startActivity(customerDetailIntent)
    }

    private fun registerHandlers() {
        buttonTransfer!!.setOnClickListener { navigateToTransactionActivity() }
    }

    private fun navigateToTransactionActivity() {
        val intentToTransactionScreen = Intent(this@ManagerDashBoard, TransactionActivity::class.java)
        startActivityForResult(intentToTransactionScreen, TransactionActivity.BALANCE_ADD_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TransactionActivity.BALANCE_ADD_REQUEST_CODE) {
            customers!!.clear()
            showCustomers()
        }
    }


}
